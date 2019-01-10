release: build-cljs
	git add ./docs
	git commit -m "release"
	git push origin master

copy-files:
	cp -r resources/public/ docs/

build-cljs: copy-files
	rm -rf docs/js
	shadow-cljs release app
