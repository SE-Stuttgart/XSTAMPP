---
author:
- Lukas Balzer
title: |
    XSTAMPP
    Setup Guide
---

How to build XSTAMPP
====================

1.  The build requires [Maven](https://maven.apache.org/) (>=3.3) and
    [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
    (>1.7) either from https://maven.apache.org/ or from the IDE.

2.  Go to the `xstampp.parent` directory in the root path of the xstampp
    project (where this file is located).

3.  Open a commandline in the xstampp.parent dir and execute

    1.  `mvn clean verify` to build xstampp with xstpa and cast already
        included.

    2.  `mvn clean install` as 3.1 but also installs xstampp on
        [user]/.m2 for usage as local dependency of other builds.

4.  The build artifacts are located in `astpa.repository/target`.

Working on XSTAMPP
==================

Language skills
---------------

1.  XSTAMPP is written in **Java 8** (depending on the Plugin)

2.  The documentation (help contents in xstampp.\[plugin\]/html) is
    provided in **html 4.0** and styled with **CSS 3**

3.  The hazx schema is given in **XMLSchema 1**

Setting up the environment
--------------------------

-   [Eclipse for RCP and RAP Developers (Plug-in
    Developement)](http://eclipse.org/downloads) (> Lunar)

-   At least JavaSE 1.8

-   To install gef (*help* —> *install new
    software* —> http://download.eclipse.org/tools/gef/updates/releases/)

-   To install nebula grid from eclipse.org

-   To install maven

-   Import/clone the XSTAMPP projects using the built-in git:

    1.  Open the *Import* Dialog selecting *File* —> *Import*

    2.  In the import menu click *Git* —> *Projects from Git*
        and follow the steps of the import wizard

-   To resolve any error messages refer to Known Issues Section

Running XSTAMPP from Eclipse
----------------------------

1.  Go to *xstampp.repository* —> *xstampp.product*.

2.  In the product editor click on *Testing* —> *Launch an
    Eclipse Application*.

3.  The run fails on the first try, which is normal because we haven’t
    included the required plugins yet.

4.  In the last step Eclipse has created a *Run configuration* for us
    which we are going to use now:

    1.  Right click on the *xstampp* project and select *Run
        As* —> *Run Configurations..*.

    2.  In the opening dialog search for the Plug-ins Tab (you may need to adjust the size of the
        window).

    3.  You can now include/exclude the xstampp plug-ins included in
        your runtime.

    4.  Finally, find/press the button *Add Required Plug-ins* and
        Apply/Run the run configuration.

![Before eclipse can successfully run xstampp the required plug-ins must
be included in the
runtime[]{data-label="fig:runConfig"}](images/runConfig.png)

Contribute
----------

-   Setting up Eclipse Preferences (open
    *Eclipse* —> *Window* —> *Preferences*):

    1.  Go to *XML* —> *XML Files* —> *Editor*:

        1.  Set the *Line width* to **120**.

        2.  Check the radio box *Indent using spaces*.

        3.  Set *Indentation size* to **4**.

    2.  Go to *Java* —> *Code Style* —> *Formatter*:

        1.  Press *Import...*.

        2.  Import the `java\_formatter.xml` in
            `<repo>/xstampp/misc/java_formatter.xml`

### Create a new plugin:

-   Contributing plugins should be named as `xstampp.<your Plugin>`.

-   Create a new plugin by clicking
    *New* —> *Others..* —> *Plug-in
    Developement* —> *Plug-in Project*.

-   Add dependencies `xstampp` and `xstampp.extension`.

-   Add the extension `xstampp.extension.steppedProcess` to your plugin.

-   Create a class implementing `IDataModel`.

-   Create stepEditors which must extend `StandartEditorPart` and
    implement `IViewBase`.

-   XSTAMPP loads the files which are selected in the load dialog or
    already located in the workspace by directly calling a load command
    registered as command in the steppedProcess extensionPoint herefore
    it needs:

    -   A load job which extends `AbstractLoadJob`.

    -   A load handler extending `AbstractHandler` which is registered as
        default handler for the load command.

    -   Let your `handler.execute()` return a new instance of your load
        job.

-   XSTAMPP uses Eclipse Tycho as build tool, to include a plugin into
    its build process it need to be configured as Maven plugin.

### Create a new Version

-   All changes must be recorded in the `CHANGELOG.md`.

-   If `misc/docu/README.tex` has been changed then:

    -   Download LaTex ([MikTex](https://miktex.org/) for Windows or
        [MacTex](http://tug.org/mactex/) for Mac).

    -   This should contain an html (for eclipse help), md (for GitHub)
        and a pdf version of the Readme this can be achived by using
        [Pandoc](https://pandoc.org).

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
    above commands to create the release files.

Known Issues {#chap:issues}
============

#### An API baseline has not been set for this Workspace

1.  Go to the Eclipse Problems View
    (Window —>  Show View —>  Problems).

2.  Right click the ’API baseline’ error.

3.  In the context menu select *Quick Fix*.

4.  A Preference Window filtered for the API Baselines opens up.

5.  In that dialog find the field *Missing API Baseline* and set it to
    *Ignore* (see figure \[fig:APIerror\])

    ![The API baseline can be
    ignored[]{data-label="fig:APIerror"}](images/ignoreAPIError.png)

#### Plugin Execution not covered by lifecycle configuration

1.  Go to
    Window —> Preferences —> Maven —> Error/Warnings.

2.  Find the line ’Plugin Execution not covered’.

3.  Set the Value to ignore, by choosing selecting ’ignore’ in the combo
    box.

4.  Click on Apply/Ok to rebuild the projects.

#### When cloning into/ importing xstampp and its sub projects to eclipse the project dependencies must be located sometimes

1.  In the Project Explorer right click on the project ’Build
    Path —> Configure Build Path’.

2.  In the ’Java Build Path’ Page click on ’Source’, by doing that java
    relocates the source folders in the projects andsets the
    dependencies.

3.  Hit Apply/Ok to store the settings.
