release: build-cljs
	emacs sanity1.org --eval "(progn (require 'org) (org-html-export-to-html))" --kill
	cp sanity1.html docs
	git add --all
	git commit -m "release"

copy-files:
	cp -r resources/public/ docs/

build-cljs: copy-files
	rm -rf docs/js
	shadow-cljs release app
