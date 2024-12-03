package com.proxy.service.core.framework.system.security.aes

import com.proxy.service.core.framework.system.security.aes.base.loader.ILoader
import com.proxy.service.core.framework.system.security.aes.base.option.IIvParameterSpec
import com.proxy.service.core.framework.system.security.aes.base.option.IPadding
import com.proxy.service.core.framework.system.security.aes.base.option.ISecretKeySpec
import com.proxy.service.core.framework.system.security.aes.impl.option.PaddingImpl
import com.proxy.service.core.framework.system.security.aes.impl.option.key.SecretKeySpecNeedIvImpl
import com.proxy.service.core.framework.system.security.aes.impl.option.key.SecretKeySpecOnlyImpl
import com.proxy.service.core.framework.system.security.aes.config.Config
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/12/2 18:06
 * @desc:
 */
object CsAesUtils {

    /**
     * 创建随机密钥
     * */
    fun createSecretKey(keySize: Int = 128): SecretKey {
        val keyGen = KeyGenerator.getInstance(Config.ALGORITHM_AES)
        keyGen.init(keySize)
        return keyGen.generateKey()
    }

    /**
     * 创建随机向量
     * */
    fun createIvParameterSpec(): IvParameterSpec {
        val secureRandom = SecureRandom()
        val iv = ByteArray(16)
        secureRandom.nextBytes(iv)
        return IvParameterSpec(iv)
    }

    /**
     * 每个明文块在加密前与前一个密文块进行异或运算，因此每个密文块都依赖于之前的所有明文块。
     * 第一个明文块需要与初始化向量（IV）进行异或。
     * 提供了较好的数据随机性，但由于依赖前一个密文块，不能并行处理。
     * */
    fun cbc(): IPadding<ISecretKeySpec<IIvParameterSpec<ILoader>>> {
        return PaddingImpl("${Config.ALGORITHM_AES}/${Config.FEED_BACK_CBC}") {
            SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, it)
        }
    }

    /**
     * 将前一个密文块加密后与当前明文块进行异或运算来生成当前密文块。
     * 适合于实时数据流的加密。
     * */
    fun cfb(): ISecretKeySpec<IIvParameterSpec<ILoader>> {
        val transformation = "${Config.ALGORITHM_AES}/${Config.FEED_BACK_CFB}/${Config.PADDING_NO}"
        return SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, transformation)
    }

    /**
     * 类似于CFB，但使用加密器输出而不是密文块作为反馈。
     * 可以预先计算加密块，适用于流式数据加密。
     * 单个位的错误只影响对应的位，因此不会传播错误。
     * */
    fun ofb(): ISecretKeySpec<IIvParameterSpec<ILoader>> {
        val transformation = "${Config.ALGORITHM_AES}/${Config.FEED_BACK_OFB}/${Config.PADDING_NO}"
        return SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, transformation)
    }

    /**
     * 使用计数器值对每个块进行加密，并将结果与明文块进行异或运算。
     * 可以并行处理多个块，提高了性能。
     * 像OFB一样，适用于流式数据加密。
     * */
    fun ctr(): ISecretKeySpec<IIvParameterSpec<ILoader>> {
        val transformation = "${Config.ALGORITHM_AES}/${Config.FEED_BACK_CTR}/${Config.PADDING_NO}"
        return SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, transformation)
    }

    /**
     * 每个明文块独立地加密为相应的密文块。
     * 相同的明文块总是会被加密成相同的密文块，容易受到模式识别攻击，不推荐用于大多数实际应用。
     * */
    fun ecb(): IPadding<ISecretKeySpec<ILoader>> {
        return PaddingImpl("${Config.ALGORITHM_AES}/${Config.FEED_BACK_ECB}") {
            SecretKeySpecOnlyImpl(Config.ALGORITHM_AES, it)
        }
    }

    /**
     * 结合了 CTR 模式的加密能力和 Galois 字段上的认证功能，提供机密性和完整性保障。
     * 支持并行操作，高效且安全，广泛应用于需要高性能和安全性的场景。
     * */
    fun gcm(): ISecretKeySpec<IIvParameterSpec<ILoader>> {
        val transformation = "${Config.ALGORITHM_AES}/${Config.FEED_BACK_GCM}/${Config.PADDING_NO}"
        return SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, transformation)
    }

    /**
     * 结合了 CTR 模式的加密和 CBC-MAC 的消息认证，先用 CBC-MAC 计算认证标签，再用 CTR 模式加密。
     * 提供认证加密，适合需要同时保证数据保密性和完整性的场合。
     * */
    fun ccm(): IPadding<ISecretKeySpec<IIvParameterSpec<ILoader>>> {
        return PaddingImpl("${Config.ALGORITHM_AES}/${Config.FEED_BACK_CCM}") {
            SecretKeySpecNeedIvImpl(Config.ALGORITHM_AES, it)
        }
    }

}