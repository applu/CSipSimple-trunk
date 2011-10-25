# Copyright (c) 2011 The WebRTC project authors. All Rights Reserved.
#
# Use of this source code is governed by a BSD-style license
# that can be found in the LICENSE file in the root of the source
# tree. An additional intellectual property rights grant can be found
# in the file PATENTS.  All contributing project authors may
# be found in the AUTHORS file in the root of the source tree.

# These defines will apply to all source files
# Think again before changing it
MY_WEBRTC_COMMON_DEFS := \
    '-DWEBRTC_TARGET_PC' \
    '-DWEBRTC_LINUX' \
    '-DWEBRTC_THREAD_RR' \
    '-DWEBRTC_CLOCK_TYPE_REALTIME' \
    '-DWEBRTC_ANDROID'
#    The following macros are used by modules,
#    we might need to re-organize them
#    '-DWEBRTC_ANDROID_OPENSLES' [module audio_device]
#    '-DNETEQ_VOICEENGINE_CODECS' [module audio_coding neteq]
#    '-DWEBRTC_MODULE_UTILITY_VIDEO' [module media_file] [module utility]
ifeq ($(TARGET_ARCH),arm)
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
MY_WEBRTC_COMMON_DEFS += \
    '-DWEBRTC_ARM_INLINE_CALLS'
endif

    #Fine - already done by typedef
    #'-DWEBRTC_ARCH_ARM'
# TODO: test if the code under next two MACROs works with generic GCC compilers
ifeq ($(ARCH_ARM_HAVE_NEON),true)
MY_WEBRTC_COMMON_DEFS += \
    '-DWEBRTC_ANDROID_ARMV7A_NEON'
else ifeq ($(ARCH_ARM_HAVE_ARMV7A),true)
MY_WEBRTC_COMMON_DEFS += \
    '-DWEBRTC_ANDROID_ARMV7A'
endif
else ifeq ($(TARGET_ARCH),x86)
MY_WEBRTC_COMMON_DEFS += \
    '-DWEBRTC_USE_SSE2'
endif

# Disable not wanted (for now) codecs
# This disable engine internal auto configuration and replace by ower settings
MY_WEBRTC_COMMON_DEFS += \
	-DWEBRTC_ENGINE_CONFIGURATIONS_H_ \
	-DWEBRTC_CODEC_ILBC 

#L16 is useless -DWEBRTC_CODEC_PCM16

ifeq ($(TARGET_ARCH_ABI),armeabi)
#Use fixed isac for armeabi
MY_WEBRTC_COMMON_DEFS += -DWEBRTC_CODEC_ISACFX
else
#Use floating isac for others
MY_WEBRTC_COMMON_DEFS += -DWEBRTC_CODEC_ISAC
endif

	
