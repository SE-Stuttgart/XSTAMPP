---
author:
- Lukas Balzer
title: |
    XSTAMPP\
    Setup Guide
---

How to build XSTAMPP
====================

1.  The build requires [Maven](https://maven.apache.org/) ($>=3.3$) and
    [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
    ($>1.7$) either from $https://maven.apache.org/$ or from the IDE

2.  go to the xstampp.parent directory in the root path of the xstampp
    project (where this file is located)

3.  open a command in the xstampp.parent dir and execute

    1.  ’mvn clean verify’ to build xstampp with xstpa and cast already
        included

    2.  ’mvn clean install’ as 3.1 but also installs xstampp on
        $[user]/.m2$ for usage as local dependency of other builds

4.  the build artifacts are located in the astpa.repository/target

Working on XSTAMPP
==================

Setting up the environment
--------------------------

-   [Eclipse for RCP and RAP Developers (Plug-in
    Developement)](http://eclipse.org/downloads)[^1] ($> Lunar$)

-   At least JavaSE 1.7

-   To install gef ( *help*$\rightarrow$*install new
    software*$\rightarrow$*http://download.eclipse.org/tools/gef/updates/releases/*)

-   To install nebula grid from eclipse.org[^2]

-   To install maven[^3]

-   import/clone xstampp projects using the included git

    1.  open the *Import* Dialog selecting *File*$\rightarrow$*Import*

    2.  in the Import menu click *Git*$\rightarrow$*Projects from Git*
        and follow the steps of the import wizard

-   To resolve upcoming error messages refer to Known Issues Section
    \[chap:issues\]

Running XSTAMPP from Eclipse
----------------------------

1.  Go to *xstampp.repository*$\rightarrow$*xstampp.product*

2.  In the product editor click on *Testing*$\rightarrow$ *Launch an
    Eclipse Application*

3.  The run fails on the first try, which is normal because we haven’t
    included the required plugins yet

4.  In the last step Eclipse has created a *Run configuration* for us
    which we are going to use now

    1.  right click on the *xstampp* project and select *Run
        As*$\rightarrow$ *Run Configurations..*

    2.  in the opening dialog search for the Plug-ins Tab (see figure
        \[fig:runConfig\])(you may need to adjust the size of the
        window)

    3.  you can now include/exclude the xstampp plug-ins included in
        your runtime

    4.  finally find/press the button *Add Required Plug-ins* and
        Apply/Run the run configuration

![Before eclipse can successfully run xstampp the required plug-ins must
be included in the
runtime[]{data-label="fig:runConfig"}](images/runConfig.png)

Contribute
----------

-   Setting up Eclipse Preferences (open
    *Eclipse*$\rightarrow$*Window*$\rightarrow$*Preferences*):

    1.  Go to *XML*$\rightarrow$*XML Files*$\rightarrow$*Editor*

        1.  set the *Line width* to **120**

        2.  check the radio box *Indent using spaces*

        3.  set *Indentation size* to **4**

    2.  Go to *Java*$\rightarrow$*Code Style*$\rightarrow$*Formatter*

        1.  Press *Import...*

        2.  Import the $java\_formatter.xml$ in
            $<repo>$*/xstampp/misc/java*$\_$*formatter.xml*

### Create a new plugin:

-   Contributing plugins should be named as *xstampp.*$<your Plugin>$

-   Create a new plugin by clicking
    *New*$\rightarrow$*Others..*$\rightarrow$*Plug-in
    Developement*$\rightarrow$*Plug-in Project*

-   Add dependencies xstampp and xstampp.extension

-   Add the extension xstampp.extension.steppedProcess to your plugin

-   Create a class implementing IDataModel

-   Create stepEditors which must extend StandartEditorPart and
    implement IViewBase

-   Xstampp loads the files which are selected in the load Dialog or
    already located in the workspace by directly calling a load command
    registered as command in the steppedProcess extensionPoint herefore
    it needs:

    -   a load job which extends AbstractLoadJob

    -   a load Handler extending AbstractHandler which is registered as
        default handler for the load command

    -   let your handler.execute() return a new instance of your load
        job

-   XSTAMPP uses Eclipse Tycho as build tool, to include a plugin into
    its build process it need to be configured as Maven plugin[^4]

### Create a new Version

-   All changes must be recorded in the $CHANGELOG.md$

-   If *misc/docu/README.tex* has been changed than:

    -   Download LaTex([MikTex](https://miktex.org/)[^5] for Windows or
        [MacTex](http://tug.org/mactex/)[^6] for Mac)

    -   This should contain an html(for eclipse help), md(for GitHub)
        and a pdf version of the Readme this can be achived by using
        [Pandoc](https://pandoc.org)[^7]

        -   `cd misc/docu`

        -   `pandoc -s README.tex -o README.pdf –toc`

        -   `pandoc -s README.tex -o README.html`

        -   `pandoc -s README.tex -o README.md`

        -   `cp README.html ../../README/html/`

        -   `cp README.pdf ../../`

-   Update the *xstampp/html/CHANGELOG.html* (using Pandoc):

    -   `cd ../..`

    -   `pandoc -s CHANGELOG.md -o CHANGELOG.html`

    -   `cp CHANGELOG.html xstampp/html/`

-   *createFiles.cmd* is a Windows batch script that executes all of the
    above commands to create the release files

Known Issues {#chap:issues}
============

#### An API baseline has not been set for this Workspace

1.  Go to the Eclipse Problems View
    ($Window\rightarrow Show View\rightarrow Problems$)

2.  Right click the ’API baseline’ error

3.  In the context menu select $Quick Fix$

4.  A Preference Window filtered for the API Baselines opens up

5.  in that Dialog find the field $Missing API Baseline$ and set it to
    $Ignore$ (see figure \[fig:APIerror\])

    ![The API baseline can be
    ignored[]{data-label="fig:APIerror"}](images/ignoreAPIError.png)

#### Plugin Execution not covered by lifecycle configuration

1.  Go to
    $Window\rightarrow Preferences\rightarrow Maven\rightarrow Error/Warnings$

2.  find the line ’Plugin Execution not covered..’

3.  Set the Value to ignore, by choosing selecting ’ignore’ in the combo
    box

4.  Click on Apply/Ok to rebuild the projects

#### When cloning into/ importing xstampp and its sub projects to eclipse the project dependencies must be located sometimes

1.  In the Project Explorer right click on the project ’Build
    Path-&gt;Configure Build Path’

2.  In the ’Java Build Path’ Page click on ’Source’, by doing that java
    relocates the source folders in the projects andsets the
    dependencies

3.  hit Apply/Ok to store the settings

[^1]: <http://eclipse.org/downloads>

[^2]: <http://download.eclipse.org/technology/nebula/snapshot/>

[^3]: <https://maven.apache.org/download.cgi>

[^4]: <http://www.vogella.com/tutorials/EclipseTycho/article.html>

[^5]: https://miktex.org/

[^6]: http://tug.org/mactex/

[^7]: https://pandoc.org
