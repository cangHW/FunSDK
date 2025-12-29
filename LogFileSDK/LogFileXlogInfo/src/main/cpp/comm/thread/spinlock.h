// Tencent is pleased to support the open source community by making Mars available.
// Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.

// Licensed under the MIT License (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at
// http://opensource.org/licenses/MIT

// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language governing permissions and
// limitations under the License.

//
//  spinlock.h
//  Android only version
//

#ifndef spinlock_h
#define spinlock_h

#include <sched.h>
#include "atomic_oper.h"

static inline void cpu_relax() {
#if defined(__arc__) || defined(__mips__) || defined(__arm__) || defined(__powerpc__)
    asm volatile("" ::: "memory");
#elif defined(__i386__) || defined(__x86_64__)
    asm volatile("rep; nop" ::: "memory");
#elif defined(__aarch64__)
    asm volatile("yield" ::: "memory");
#endif
}

class SpinLock {
 public:
    typedef uint32_t handle_type;

 private:
    enum state { initial_pause = 2, max_pause = 16 };

    uint32_t state_;

 public:
    SpinLock() : state_(0) {
    }

    bool trylock() {
        return (atomic_cas32((volatile uint32_t*)&state_, 1, 0) == 0);
    }

    bool lock() {
        unsigned int pause_count = initial_pause;
        while (!trylock()) {
            if (pause_count < max_pause) {
                for (unsigned int i = 0; i < pause_count; ++i) {
                    cpu_relax();
                }
                pause_count += pause_count;
            } else {
                pause_count = initial_pause;
                sched_yield();
            }
        }
        return true;
    }

    bool unlock() {
        atomic_write32((volatile uint32_t*)&state_, 0);
        return true;
    }

    uint32_t* internal() {
        return &state_;
    }

 private:
    SpinLock(const SpinLock&);
    SpinLock& operator=(const SpinLock&);
};

#endif
