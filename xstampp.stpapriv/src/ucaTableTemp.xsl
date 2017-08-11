<?xml version="1.0"?>

<!DOCTYPE stylesheet>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    	<!-- ############################################################-->	
	<!-- ################### Templates of the PDF ###################-->
	<!-- ############################################################-->
	
    <!-- Page layout -->
    <xsl:template name="layout">
    
		<!-- Page-Master for the common pages -->
		<fo:simple-page-master margin="5mm 5mm 5mm 5mm"
			page-height="297mm" page-width="210mm" master-name="titel">
			<fo:region-body margin="5mm 0mm 10mm 5mm" />
		</fo:simple-page-master>
		
		<fo:simple-page-master margin="5mm 5mm 5mm 5mm"
			page-width="auto" page-height="auto" master-name="auto">
			<fo:region-body margin="5mm 0mm 10mm 5mm" />
		</fo:simple-page-master>
			
		<fo:simple-page-master margin="5mm 5mm 10mm 5mm"
				page-height="210mm" page-width="297mm" master-name="A4Landscape">
				<fo:region-body margin="15mm 0mm 10mm 5mm" />
				<fo:region-before extent="20mm" display-align="before" />
				<fo:region-after extent="10mm" display-align="after" />
		</fo:simple-page-master>
				
		<fo:simple-page-master margin="5mm 5mm 10mm 5mm"
				page-height="297mm" page-width="210mm" master-name="A4">
				<fo:region-body margin="15mm 0mm 10mm 5mm" />
				<fo:region-before extent="20mm" display-align="before" />
				<fo:region-after extent="10mm" display-align="after" />
		</fo:simple-page-master>
	</xsl:template>	
	
	<!-- ################### Head ################### -->
	<xsl:template name="stpasecHead">
      <xsl:param name="pdfTitle" select="NON"/> 
		<fo:table background-color="#DEDEDE">
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="4px">
								<fo:block font-size="10pt">
									<xsl:value-of select="projectdata/projectName" />
								</fo:block>
					</fo:table-cell>
					
							<fo:table-cell padding="4px">
								<fo:block font-size="10pt">
									<xsl:value-of select="$pdfTitle" />
								</fo:block>
							</fo:table-cell>
							
					
					
					<fo:table-cell padding="4px">
						<fo:block font-size="10pt" text-align="right">
							<xsl:value-of select="exportinformation/date" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	<!-- ################### Footer ################### -->
	<xsl:template name="stpasecFooter">
		<fo:table>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell background-color="#DEDEDE" padding="4px">
						<!-- Page Numbering -->
						<fo:block font-size="10pt" text-align="center">
							Page &#x0020;
							<fo:page-number />
							of
							<fo:page-number-citation-last ref-id="total"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding="2px">
						<fo:block font-size="8pt">Created with STPA-Priv</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Heading-Background-Color ################### -->
	<xsl:template name="headTheme">
		<xsl:if test="exportinformation/backgroundColor">
			<xsl:attribute name="background-color">
				<xsl:value-of select="exportinformation/backgroundColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	
	<!-- ################### Font-Color ################### -->
	<xsl:template name="fontTheme">
		<xsl:if test="exportinformation/fontColor">
			<xsl:attribute name="color">
				<xsl:value-of select="exportinformation/fontColor" />
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="hazardLinkColor">
		<xsl:choose>
			<xsl:when test="links = 'Not Hazardous'">
				<fo:block color="#2D7500">
					<xsl:value-of select="links" />
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block color="#820000">
					<xsl:value-of select="links" />
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ################### Losses-Table ################### -->
	<xsl:template name="accidentsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="7%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="50%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="13%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Title</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Related Vulnera-bilities</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some accidents are defined -->
					<xsl:when test="vulloss/losses/loss">
						<xsl:for-each select="vulloss/losses/loss">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block >
										L-<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="links" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no accidents defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Hazard-Table ################### -->
	<xsl:template name="hazardTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="4" column-width="13%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF" padding="3px">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Title</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Related Adverse Consequences</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="vulloss/vulnerabilities/vulnerability">
						<xsl:for-each select="vulloss/vulnerabilities/vulnerability">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										V-<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="links" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no hazards defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Safety Constraints Table ################### -->
	<xsl:template name="safetyConstraintsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="63%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Privacy Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some Safety Constraints are defined -->
					<xsl:when test="sds/safetyConstraints/safetyConstraint">
						<xsl:for-each select="sds/safetyConstraints/safetyConstraint">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										PC0.<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no Safety Constraints defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>	
	
	<!-- ################### System Goals Table ################### -->
	<xsl:template name="systemGoalsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="63%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">System Goal</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some System Goals are defined -->
					<xsl:when test="sds/systemGoals/systemGoal">
						<xsl:for-each select="sds/systemGoals/systemGoal">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no System Goals defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
		<!-- ################### Results Table ################### -->
	<xsl:template name="resultsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="10%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="5%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="35%"
			border-style="none" />
		<fo:table-column column-number="4" column-width="20%"
			border-style="none" />			
		<fo:table-column column-number="5" column-width="10%"
			border-style="none" />
		<fo:table-column column-number="6" column-width="10%"
			border-style="none" />
		<fo:table-column column-number="7" column-width="10%"
			border-style="none" />
								
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">STPA-Priv Step</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Privacy Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Corresponding Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Privacy Related</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Security Related</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Safety Related</fo:block>
					</fo:table-cell>															
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some System Goals are defined -->
					<xsl:when test="crc/constraintResults/constraintResult">
						<xsl:for-each select="crc/constraintResults/constraintResult">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="secstep" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="secid" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="secConstraint" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="corrSecConstraint" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										 <xsl:choose>
        								 <xsl:when test="privRelated='true'">
           									 Yes 
        									 </xsl:when>
        									 <xsl:otherwise>
        									  No 
       										  </xsl:otherwise>
      										 </xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										 <xsl:choose>
        								 <xsl:when test="secRelated='true'">
           									 Yes 
        									 </xsl:when>
        									 <xsl:otherwise>
        									  No 
       										  </xsl:otherwise>
      										 </xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
									 <xsl:choose>
        								 <xsl:when test="safRelated='true'">
           									 Yes 
        									 </xsl:when>
        									 <xsl:otherwise>
        									 No
       										  </xsl:otherwise>
      										 </xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no System Goals defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!-- ################### Design Requirements Table ################### -->
	<xsl:template name="designRequirementsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
		border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="65%"
			border-style="none" />
	
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
			<xsl:call-template name="headTheme" />
			<xsl:call-template name="fontTheme" />
			<fo:table-row>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">No.</fo:block>
				</fo:table-cell>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Design Requirement</fo:block>
				</fo:table-cell>
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Description</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
	
		<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
			<xsl:choose>
				<!-- Checks whether some Design Requirements are defined -->
				<xsl:when test="sds/designRequirements/designRequirement">
					<xsl:for-each select="sds/designRequirements/designRequirement">
						<fo:table-row border="none">
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
							</xsl:if>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="number" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="title" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="description" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</xsl:when>
				<!-- If there are no Design Requirements defined the first row -->
				<!-- should be filled with strokes -->
				<xsl:otherwise>
					<fo:table-row>
						<fo:table-cell padding="3px">
							<fo:block>
								&#x2014; </fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block>
								&#x2014; </fo:block>
						</fo:table-cell>
						<fo:table-cell padding="3px">
							<fo:block>
								&#x2014; </fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:otherwise>
			</xsl:choose>
		</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Control Actions Table ################### -->
	<xsl:template name="controlActionsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="65%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
			
            <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether there are some Control Actions defined -->
					<xsl:when test="secCac/controlactions/controlaction">
						<xsl:for-each select="secCac/controlactions/controlaction">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="number" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no Control Actions defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	
	<!-- ################### Corresponding Safety Constraints Table ################### -->
	<xsl:template name="correspondingSafetyConstraintsTable">
	
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="10%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="40%"
				border-style="none" />
			<fo:table-column column-number="1" column-width="10%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="40%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                     		<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						   ID
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                     		<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						   Privacy-Compromising Control Actions
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                     		<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						   ID
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                                 <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						Corresponding Privacy Constraints
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:choose>
					<!-- Checks for hazardous UCA -->
					<xsl:when
						test="secCac/controlactions/controlaction/unsecurecontrolactions/unsecurecontrolaction[links != 'Not Hazardous']">
						<xsl:for-each
							select="secCac/controlactions/controlaction/unsecurecontrolactions/unsecurecontrolaction[links != 'Not Hazardous']">
	
								<fo:table-row border="none">
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
									</xsl:if>
									<fo:table-cell padding="3px">
										<fo:block>
										   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											PCA1.<xsl:value-of select="identifier" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block>
										   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											<xsl:value-of select="description" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block>
										   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											PC1.<xsl:value-of select="identifier" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
                                 <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
											<xsl:value-of select="correspondingSecurityConstraint/text" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>						
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no hazardous UCA defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Causal Factors Table ################### -->
	<xsl:template name="causalFactorsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="15%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="85%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
					<!-- Sets the PDF-Theme-Color -->
					<xsl:call-template name="headTheme"/>
					<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Component</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
							<fo:table border="none" space-after="30pt">
								<fo:table-column column-number="1" column-width="15%"
									border-style="none" />
								<fo:table-column column-number="2" column-width="85%"
									border-style="none" />
	
								<fo:table-header border="none" background-color="#1A277A"
									color="#FFFFFF">
									<!-- Sets the PDF-Theme-Color -->
									<xsl:call-template name="headTheme"/>
									<xsl:call-template name="fontTheme"/>
									<fo:table-row>
										<fo:table-cell>
											<fo:block font-weight="bold">Causal Factor</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block font-weight="bold">
												<fo:table border="none" space-after="30pt">
													<fo:table-column column-number="1" column-width="20%"
														border-style="none" />
													<fo:table-column column-number="2" column-width="20%"
														border-style="none" />
													<fo:table-column column-number="3" column-width="20%"
														border-style="none" />
													<fo:table-column column-number="4" column-width="20%"
														border-style="none" />
													<fo:table-column column-number="5" column-width="20%"
														border-style="none" />
						
													<fo:table-header border="none" background-color="#1A277A"
														color="#FFFFFF">
														<!-- Sets the PDF-Theme-Color -->
														<xsl:call-template name="headTheme"/>
														<xsl:call-template name="fontTheme"/>
														<fo:table-row>
															<fo:table-cell>
																<fo:block font-weight="bold">Privacy-Compromising Control Action</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block font-weight="bold">Vulnera-bility Links</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block font-weight="bold">Scenario</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block font-weight="bold">Privacy Constraint</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block font-weight="bold">Notes</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-header>
													<fo:table-body>
														<!-- Empty Table-Body -->
														<fo:table-row>
															<fo:table-cell>
																<fo:block/>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block/>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block/>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block/>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block/>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>
									<!-- Empty Table-Body -->
									<fo:table-row>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
	
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
				<!-- Checks if there are some components for the CausalFactors-Table -->
					<xsl:when test="causalfactor/causalComponents">
						<xsl:for-each select="causalfactor/causalComponents/entry/value">
							<xsl:choose>
								<xsl:when test="causalFactors">
									<fo:table-row border-bottom="2pt solid black"
										border-top="2pt solid black">
										<fo:table-cell padding="4px" background-color="#FFFFFF"
											color="#000000" border-right="2pt solid black">
											<fo:block  font-weight="bold">
												<xsl:value-of select="title" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
										
											<!-- ***** CausalFactors with its relatives ***** -->
											<fo:block >
											<xsl:call-template name="causalFactorSubTable"/> 
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:when>
							</xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no CausalFactor-Components defined the first row -->
					<!-- should be filled with strokes -->
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014; </fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	<xsl:template name="causalFactorSubTable">
		<fo:table>
			<fo:table-column column-number="1" column-width="15%"
				border-style="solid" />
			<fo:table-column column-number="2" column-width="85%"
				border-style="solid" />
			<fo:table-body>
			<xsl:choose>
				<xsl:when test="causalFactors">
					<xsl:for-each select="causalFactors/factor">
						<fo:table-row border-bottom="2pt solid black">
							<fo:table-cell>
								<fo:block >
									<xsl:value-of select="text" />
								</fo:block>
							</fo:table-cell>	
							<fo:table-cell padding="0px">
								<fo:block >
									<xsl:call-template name="causalFactorRelatives"/>
								</fo:block>
							</fo:table-cell>				
						</fo:table-row>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell>
								<fo:block >
								</fo:block>
							</fo:table-cell>	
							<fo:table-cell>
								<fo:block >
								</fo:block>
							</fo:table-cell>				
						</fo:table-row>
				</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### Causal Factors-Sub-Table ################### -->
	<!-- Every Causal Factor and its relative can be found in this sub-table -->
	<xsl:template name="causalFactorRelatives">
    <fo:table>
			<fo:table-column column-number="1" column-width="20%"/>
			<fo:table-column column-number="2" column-width="20%"/>
			<fo:table-column column-number="3" column-width="40%"/>
			<fo:table-column column-number="4" column-width="20%"
				border-style="none" />
			<fo:table-body>
				<xsl:choose>
					<!-- Checks if there are some CausalFactors defined -->
					<!-- for a component. -->
					<xsl:when test="causalEntries">
					<xsl:for-each select="causalEntries/causalEntry">
						<fo:table-row>
							<!-- Sets the row-colour -->
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
							</xsl:if>
							
							<!-- Causal Factor -->
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="ucaDescription" />
								</fo:block>
							</fo:table-cell>
							
							<!-- Hazard Links -->
							<fo:table-cell padding="4px">
								<fo:block>
								<xsl:value-of select="hazardLinks"/>
								</fo:block>
							</fo:table-cell>
							
							<!-- Safety Constraint -->
							<fo:table-cell padding="3px">
								<xsl:call-template name="causalFactorScenarioTable"/>
							</fo:table-cell>
							
							<!-- Notes or Rationale -->
							<fo:table-cell padding="3px">
								<xsl:choose>
									<xsl:when test="note = 'Note'">
										<fo:block>
											&#x2014;
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block>
											<xsl:value-of select="note" />
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>	
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					</xsl:when>
				
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									&#x2014;
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
								&#x2014;
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
								&#x2014;
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="3px">
								<fo:block>
								&#x2014;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>			
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="causalFactorScenarioTable">
		<fo:table>
			
			<fo:table-column column-number="1" column-width="50%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="50%"
				border-style="none" />
			<fo:table-body>
				<xsl:choose>
					<xsl:when test="scenarioEntries/scenarioEntry">
						<xsl:for-each select="scenarioEntries/scenarioEntry">
							<fo:table-row border-bottom="1px solid black">
								<fo:table-cell  padding="3px">
									<fo:block >
										<xsl:value-of select="description" />
									</fo:block>
								</fo:table-cell>	
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="constraint"/>
									</fo:block>
								</fo:table-cell>				
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<xsl:value-of select="constraintText"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
								&#x2014;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
<!-- ################### Unsecure Control Actions Table (UCA) ################### -->
	<xsl:template name="ucaTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/>       
    <xsl:param name="value"/>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="5" column-width="20%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" >
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Not providing causes vulnerability </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Providing causes vulnerability </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Wrong timing or order causes vulnerability </fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Stopped too soon or applied too long
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks if there are some Control Actions already defined -->
					<xsl:when test="secCac/controlactions/controlaction">
						<xsl:for-each select="secCac/controlactions/controlaction">
							<fo:table-row border-bottom="2pt solid black"
								border-top="2pt solid black">
								<fo:table-cell padding="4px" background-color="#FFFFFF"
									color="#000000" border-right="2pt solid black">
									<fo:block font-weight="bold">
										<xsl:value-of select="title" />
									</fo:block>
								</fo:table-cell>
								
								<!-- Creates for each Control Action a Unsafe Control Action Cell -->
								
								<!-- NOT GIVEN-Cell -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- NOT GIVEN - SubTable -->
									<xsl:call-template name="notGiven"/>
								</fo:table-cell>
			
								<!-- GIVEN INCORRECTLY-Cell -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- GIVEN_INCORRECTLY - SubTable -->
									<xsl:call-template name="givenIncorrectly"/>
								</fo:table-cell>
			
								<!-- ########## WRONG TIMING -Cell ########## -->
								<fo:table-cell padding="4px" background-color="#FFFFFF">
									<!-- WRONG TIMING - SubTable -->
									<xsl:call-template name="wrongTiming"/>
								</fo:table-cell>
			
								<!-- ########## STOPPED TOO SOON-Cell ########## -->
								<fo:table-cell padding="4px">
									<!-- STOPPED TOO SOON - SubTable -->
									<xsl:call-template name="stoppedTooSoon"/>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</xsl:when>
						
						<!-- If there are no Control Actions defined the first row -->
						<!-- should be filled with strokes -->
						<xsl:otherwise>
							<fo:table-row>
								<fo:table-cell padding="3px">
									<fo:block>
										&#x2014; </fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										&#x2014; </fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										&#x2014; </fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										&#x2014; </fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										&#x2014; </fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:otherwise>
					</xsl:choose>
				</fo:table-body>
			</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: NOT GIVEN ################### -->
	<xsl:template name="notGiven">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>	
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each
					select="unsecurecontrolactions/unsecurecontrolaction[type='NOT_GIVEN']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: GIVEN INCORRECTLY ################### -->
	<xsl:template name="givenIncorrectly">
	<fo:table>
		<fo:table-column column-width="100%" />
		<!-- empty row -->
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:block/>		
				</fo:table-cell>
			</fo:table-row>
			<!-- For each "GIVEN_INCORRECTLY"-UCA, a new row shall be generated -->
			<xsl:for-each
				select="unsecurecontrolactions/unsecurecontrolaction[type='GIVEN_INCORRECTLY']">
				<fo:table-row>
					<!-- Sets the row-colour -->
					<xsl:if test="position() mod 2 = 0">
						<xsl:attribute name="background-color">
				#D9D9D9
				</xsl:attribute>
					</xsl:if>
					<fo:table-cell padding="3px">
							<!-- Chooses the Link-Colour depending on the hazardous-state -->
							<xsl:call-template name="ucaHazLinkColor"/>
					</fo:table-cell>
				</fo:table-row>
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: WRONG TIMING ################### -->
	<xsl:template name="wrongTiming">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>
	
						
					</fo:table-cell>
				</fo:table-row>
				<!-- For each "WRONG-TIMING"-UCA, a new row shall be generated -->
				<xsl:for-each
					select="unsecurecontrolactions/unsecurecontrolaction[type='WRONG_TIMING']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: STOPPED TOO SOON ################### -->
	<xsl:template name="stoppedTooSoon">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty Row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>
					</fo:table-cell>
				</fo:table-row>
				<!-- For each "STOPPED TOO SOON"-UCA, a new row shall be generated -->
				<xsl:for-each
					select="unsecurecontrolactions/unsecurecontrolaction[type='STOPPED_TOO_SOON']">
					<fo:table-row>
						<!-- Sets the row-colour -->
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
						#D9D9D9
						</xsl:attribute>
							</xsl:if>
							<fo:table-cell padding="3px">
								<!-- Chooses the Link-Colour depending on the hazardous-state -->
								<xsl:call-template name="ucaHazLinkColor"/>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
	</xsl:template>
	
	<!-- ################### UCA - Hazardous-Color-Chooser ################### -->
	<xsl:template name="ucaHazLinkColor">
	
		<fo:block page-break-inside="avoid">
			<fo:table>
				<fo:table-column column-width="100%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							
							<fo:block color="#820000">
								<xsl:if test="identifier!=''">
								PCA1.<xsl:value-of select="identifier" />
								</xsl:if>
								
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell padding="3px">
							<fo:block>
								<xsl:value-of select="description" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<xsl:choose>
								<xsl:when test="links = 'Not Hazardous'">
									<fo:block color="#2D7500">
										&#x005B;
										<xsl:value-of select="links" />
										&#x005D;
									</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block color="#820000">
										&#x005B;
										<xsl:value-of select="links" />
										&#x005D;
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>