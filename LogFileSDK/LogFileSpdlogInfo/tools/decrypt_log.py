#!/usr/bin/env python3
"""
LogFileSpdlogInfo 日志解密工具

解密由 EncryptedFileSink 生成的加密日志文件。
算法: ChaCha20-Poly1305 (IETF, RFC 8439)
文件格式: 20字节 header + 逐行加密记录 [len:4][nonce:12][ciphertext:N][tag:16]

用法
python decrypt_log.py --key "my_secret_password" --input log.log --output plain.txt
"""

import sys
import os
import struct
import argparse

# 自动查找脚本同目录下的 .venv
_script_dir = os.path.dirname(os.path.abspath(__file__))
_venv_site = os.path.join(_script_dir, '.venv', 'lib')
if os.path.isdir(_venv_site):
    import glob
    _paths = glob.glob(os.path.join(_venv_site, 'python*', 'site-packages'))
    for _p in _paths:
        if _p not in sys.path:
            sys.path.insert(0, _p)

try:
    from nacl.bindings import (
        crypto_aead_chacha20poly1305_ietf_decrypt,
        crypto_aead_chacha20poly1305_ietf_KEYBYTES,
        crypto_aead_chacha20poly1305_ietf_NPUBBYTES,
        crypto_aead_chacha20poly1305_ietf_ABYTES,
    )
except ImportError:
    print("错误: 缺少依赖 pynacl，请执行:")
    print(f"  python3 -m venv {os.path.join(_script_dir, '.venv')}")
    print(f"  {os.path.join(_script_dir, '.venv', 'bin', 'pip')} install pynacl")
    sys.exit(1)

MAGIC = 0x454C4F47  # "ELOG"
HEADER_SIZE = 20
FLAG_ENCRYPTED = 0x0001


def read_header(f):
    data = f.read(HEADER_SIZE)
    if len(data) < HEADER_SIZE:
        return None
    magic, version, flags, file_id = struct.unpack_from('<IHHI', data, 0)
    # reserved 8 bytes ignored
    return {
        'magic': magic,
        'version': version,
        'flags': flags,
        'file_id': file_id,
    }


def decrypt_file(input_path, output_path, key):
    with open(input_path, 'rb') as fin:
        header = read_header(fin)
        if header is None:
            print(f"[ERROR] {input_path}: 文件太小，无法读取 header")
            return False

        if header['magic'] != MAGIC:
            print(f"[ERROR] {input_path}: 无效的 magic (0x{header['magic']:08X})")
            return False

        encrypted = (header['flags'] & FLAG_ENCRYPTED) != 0

        with open(output_path, 'w', encoding='utf-8') as fout:
            if not encrypted:
                # 明文文件，跳过 header 直接输出
                fout.write(fin.read().decode('utf-8', errors='replace'))
                return True

            line_no = 0
            decrypted_count = 0
            failed_count = 0

            while True:
                len_bytes = fin.read(4)
                if len(len_bytes) < 4:
                    break

                record_len = struct.unpack('<I', len_bytes)[0]
                record = fin.read(record_len)
                if len(record) < record_len:
                    # 不完整记录（崩溃尾部）
                    break

                line_no += 1

                if record_len < 12 + 16:
                    failed_count += 1
                    print(f"[WARN] Line {line_no}: 记录太短，跳过")
                    continue

                nonce = record[:12]
                ciphertext_with_tag = record[12:]

                try:
                    plaintext = crypto_aead_chacha20poly1305_ietf_decrypt(
                        ciphertext_with_tag, None, nonce, key)
                    fout.write(plaintext.decode('utf-8', errors='replace'))
                    decrypted_count += 1
                except Exception:
                    failed_count += 1
                    print(f"[WARN] Line {line_no}: 解密失败，跳过")
                    continue

            print(f"[INFO] {input_path}: 解密完成, "
                  f"成功 {decrypted_count} 行, 失败 {failed_count} 行")

    return True


def load_key(args):
    import hashlib

    if args.key:
        passphrase = args.key
    elif args.key_file:
        with open(args.key_file, 'r') as f:
            passphrase = f.read().strip()
    else:
        print("错误: 必须提供 --key 或 --key-file")
        sys.exit(1)

    key = hashlib.sha256(passphrase.encode('utf-8')).digest()
    return key


def main():
    parser = argparse.ArgumentParser(
        description='LogFileSpdlogInfo 加密日志解密工具')
    parser.add_argument('--key', type=str, help='加密密钥 (与 setEncryptionKey 传入的字符串一致)')
    parser.add_argument('--key-file', type=str, help='从文件读取密钥')
    parser.add_argument('--input', '-i', type=str, required=True,
                        help='输入文件或目录路径')
    parser.add_argument('--output', '-o', type=str, required=True,
                        help='输出文件或目录路径')
    args = parser.parse_args()

    key = load_key(args)

    if os.path.isdir(args.input):
        os.makedirs(args.output, exist_ok=True)
        for filename in sorted(os.listdir(args.input)):
            filepath = os.path.join(args.input, filename)
            if os.path.isfile(filepath):
                out_name = os.path.splitext(filename)[0] + '_decrypted.txt'
                out_path = os.path.join(args.output, out_name)
                decrypt_file(filepath, out_path, key)
    elif os.path.isfile(args.input):
        decrypt_file(args.input, args.output, key)
    else:
        print(f"错误: 输入路径不存在: {args.input}")
        sys.exit(1)


if __name__ == '__main__':
    main()
