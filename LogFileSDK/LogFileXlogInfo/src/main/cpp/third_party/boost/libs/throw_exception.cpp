// throw_exception.cpp
// Boost throw_exception 实现

#include <boost/config.hpp>
#include <exception>
#include <stdexcept>

namespace mars_boost {

#ifdef BOOST_NO_EXCEPTIONS

void throw_exception(std::exception const & e) {
    // 在禁用异常的环境中，直接终止
    std::terminate();
}

#else

void throw_exception(std::exception const & e) {
    throw e;
}

#endif

}  // namespace mars_boost


