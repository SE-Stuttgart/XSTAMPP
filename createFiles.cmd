del CHANGELOG.html
del README.md
del README.pdf
del Readme.pdf
del xstampp\html\README.html
del xstampp\html\CHANGELOG.html
cd misc/docu
pandoc -s README.tex -o README.pdf --toc
pandoc -s README.tex -o README.html
pandoc -s README.tex -o README.md markdown_strict
xcopy /f /y /g /k README.html ..\..\xstampp\html\
xcopy /f /y /g /k README.pdf ..\..\
xcopy /f /y /g /k README.md ..\..\
cd ../..
pandoc -s CHANGELOG.md -o CHANGELOG.html
xcopy /f /y /g /k CHANGELOG.html xstampp\html\