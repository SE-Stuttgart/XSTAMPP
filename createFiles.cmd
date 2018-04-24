del CHANGELOG.html
del README.pdf
del Readme.pdf
del xstampp\html\README.html
del xstampp\html\CHANGELOG.html
pandoc -s README.md -o README.pdf --toc
pandoc -s README.md -o xstampp\html\README.html
pandoc -s CHANGELOG.md -o xstampp\html\CHANGELOG.html