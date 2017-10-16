cd misc/docu
pandoc -s README.tex -o README.pdf --toc
pandoc -s README.tex -o README.html
pandoc -s README.tex -o README.md
xcopy /f /y /g /k README.html ..\..\xstampp\html\
xcopy /f /y /g /k README.pdf ..\..\
xcopy /f /y /g /k README.md ..\..\
cd ../..
pandoc -s CHANGELOG.md -o CHANGELOG.html