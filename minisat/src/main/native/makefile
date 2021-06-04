#-----------------------------------------------------------------------#
#- GLOBAL DEFS ---------------------------------------------------------#
#-----------------------------------------------------------------------#

# Keep this as generic as possible.

NAME=${library.name}

# Avoid hardcoding the version number in this file.  This allows us to just
# drop a new distribution package into this directory without changing
# anything else. For other more complicated set-ups / solvers something like
# 'VERSION=960' should also work, if you have the appropriate tar file
# available, e.g., 'picosat-960.tar.gz', which extracts into 'picosat-960'.

# NOTE: requires GNU make extension '$(shell ...)'.

VERSION=$(shell ls $(NAME)-*.tar.gz | tail -1 | \
		sed -e 's,$(NAME)-,,' -e 's,.tar.gz,,')

#-----------------------------------------------------------------------#
# Solver signatures have to be both valid file names and C symbols.
# Since Picosat uses a dash '-' for the package name, we have to
# differentiate between with (directory / package name) and without
# dash (the signature).

SIG		= $(NAME)$(VERSION)
DIR		= $(NAME)-$(VERSION)
TARGET	?= dlib

#-----------------------------------------------------------------------#

export CXX 		?= g++
export AR		?= ar
# to build universal binary on MacOS, we switch ar rc -> -static -o  
AR += -rcs
export MAKE		?= make
export CXXFLAGS	?= -Wall -DNDEBUG -O3 -fPIC
CXXFLAGS += -DVERSION=\"$(VERSION)\"
# zlib is not provided for mingw32
ZLIB_VERSION	?= 1.2.11

#-----------------------------------------------------------------------#
#- REQUIRED TOP RULES --------------------------------------------------#
#-----------------------------------------------------------------------#

all: $(TARGET)

clean:
	rm -rf $(DIR) zlib-$(ZLIB_VERSION)
	rm -f *.o *.a *.dylib *.so *.dll

#-----------------------------------------------------------------------#
#- INVISIBLE INTERNAL SUB RULES ----------------------------------------#
#-----------------------------------------------------------------------#

# enable verbatim output
ifeq ($(VERB),)
VB=@
else
VB=
endif

# cross-compilation
ifeq ($(CHOST),)
else
	CROSS_PREFIX = $(CHOST)-
	CXX := $(CROSS_PREFIX)$(CXX)
	AR := $(CROSS_PREFIX)$(AR)
	UNAME = $(shell echo "$(CHOST)" | sed -e 's/^[^-]*-\([^-]*\)\s*$$/\1/' -e 's/^[^-]*-[^-]*-\([^-]*\)\s*$$/\1/' -e 's/^[^-]*-[^-]*-\([^-]*\)-.*$$/\1/')
endif

# determine operating system name
UNAME ?= $(shell uname -s)

# the location of this makefile (independent of the current directory)
MROOT := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))

ifeq ($(UNAME), Darwin)
  DLIB = lib$(NAME).dylib
  # build universal "fat" binaries
  export CXXFLAGS += -arch x86_64 -arch arm64
  AR = libtool -static -o
else ifeq ($(UNAME), Linux)
  DLIB = lib$(NAME).so
else ifeq ($(UNAME), mingw32)
  DLIB = $(NAME).dll  
  CXXFLAGS += -I$(MROOT)/zlib-$(ZLIB_VERSION) -L$(MROOT)/zlib-$(ZLIB_VERSION)
  # MinGW does not ship with zlib
  LIBS = zlib-$(ZLIB_VERSION)  
else
  $(error Platform not supported: $(UNAME))
endif 

STLIB=$(DIR)/build/release/lib/lib$(NAME).a
GLUE=$(DIR)/build/release/minisat/core/ipasir$(NAME)glue.o

dlib: $(DLIB)

# shared library for Mac OS
lib$(NAME).dylib: $(STLIB)
	$(VB) $(CXX) $(CXXFLAGS) -g -I. -o $@ -dynamiclib -install_name $@ $(GLUE) $^

# shared library for Linux
lib$(NAME).so: $(STLIB)
	$(VB) $(CXX) $(CXXFLAGS) -g -I. -o $@ -shared -Wl,-soname,$@ $(GLUE) $^
	
# shared library for MinGW
$(NAME).dll: $(STLIB)
	$(VB) $(CXX) $(CXXFLAGS) -static-libgcc -static-libstdc++ -g -I. -o $@ -shared -Wl,--subsystem,windows $(GLUE) $^ 		

# static solver library
$(STLIB): $(DIR) $(DIR).patch $(LIBS)	
	@#
	@# configure and build library
	@#
	$(VB) $(MAKE) -C $(DIR) lr

$(DIR): $(DIR).tar.gz
	@#
	@# extract library
	@#
	$(VB) tar xf $(DIR).tar.gz
	@#
	@# apply patch for set_terminate and learn_callback
	@#
	$(VB) patch -p0 < $(DIR).patch
	@#
	@# patch verbose output
	@#
	$(VB) sed -i.bak -e 's,"\([=|]\),"c [${SIG}] \1,' $(DIR)/minisat/core/Main.cc
	$(VB) sed -i.bak -e 's,"\([=|]\),"c [${SIG}] \1,' $(DIR)/minisat/core/Solver.cc
	@#
	@# patch archiver
	@#
	$(VB) sed -i.bak -e's/$$(AR) -rcs/$$(AR)/' $(DIR)/Makefile
	@#
	@# copy glue code for ipasir
	@#
	$(VB) cp ipasir.h $(DIR)/minisat/core/
	$(VB) cp ipasir$(NAME)glue.cc $(DIR)/minisat/core/

# zlib
zlib-$(ZLIB_VERSION): zlib-$(ZLIB_VERSION).tar.gz
	tar xzf $<
	@#
	@# patch makefile for mingw
	@#
	$(VB) sed -i.bak -e s/"PREFIX ="/"PREFIX = $(CROSS_PREFIX)"/ zlib-$(ZLIB_VERSION)/win32/Makefile.gcc
	$(VB) cd zlib-$(ZLIB_VERSION); $(MAKE) -f win32/Makefile.gcc
		

zlib-$(ZLIB_VERSION).tar.gz:
	$(VB) wget http://zlib.net/zlib-$(ZLIB_VERSION).tar.gz

#-----------------------------------------------------------------------#

.FORCE:
.PHONY: all clean
