SUBMIT := src/interp.ml
TIMESTAMP := $(shell /bin/date "+%Y-%m-%d-%H:%M:%S")
ZIPNAME := hw3_$(TIMESTAMP).zip

all:
	dune build

.PHONY: test
test:
	dune runtest

zip: $(SUBMIT)
	zip '$(ZIPNAME)' $(SUBMIT)

.PHONY: clean
clean:
	dune clean

.PHONY: utop
utop:
	dune utop
