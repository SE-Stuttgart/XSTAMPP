<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright (C) 2017 Jaqueline Patzek, Patrick Wickenhäuser,Lukas Balzer StuPro 2013 / 2014
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
  
  Contributors:
      Jaqueline Patzek, Patrick Wickenhäuser,Lukas Balzer  - initial API and implementation
-->
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
	<xsl:template name="astpaHead">
      <xsl:param name="pdfTitle" select="NON"/> 
      <xsl:param name="titleSize" select="14"/> 
      
		<fo:table background-color="#DEDEDE">
			<fo:table-body>
				<fo:table-row>
           <xsl:attribute name="font-size"><xsl:value-of select="$titleSize" />pt</xsl:attribute>
					<fo:table-cell padding="4px">
								<fo:block >
									<xsl:value-of select="projectdata/projectName" />
								</fo:block>
					</fo:table-cell>
					
							<fo:table-cell padding="4px" text-align="center">
								<fo:block>
									<xsl:value-of select="$pdfTitle" /> - 
									<fo:retrieve-marker retrieve-class-name="header_value"/>
								</fo:block>
								
							</fo:table-cell>
							
					
					
					<fo:table-cell padding="4px">
						<fo:block text-align="right">
							<xsl:value-of select="exportinformation/date" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	<!-- ################### Footer ################### -->
	<xsl:template name="astpaFooter">
      <xsl:param name="titleSize" select="14"/> 
		<fo:table>
			<fo:table-body>
				<fo:table-row font-size="6pt">
					<fo:table-cell padding="4px">
						<fo:block text-align="left">
							<fo:retrieve-marker retrieve-class-name="note"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row background-color="#DEDEDE">
           <xsl:attribute name="font-size"><xsl:value-of select="$titleSize" />pt</xsl:attribute>
					<fo:table-cell padding="4px">
						<fo:block>Created with A-STPA 3.1.0</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="4px">
						<!-- Page Numbering -->
						<fo:block text-align="center">
							Page &#x0020;
							<fo:page-number />
							of
							<fo:page-number-citation-last ref-id="total"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="2px">
						<fo:block></fo:block>
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
	
	<!-- ################### Accidents-Table ################### -->
	<xsl:template name="accidentsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Accidents
    	</fo:marker>
      	<fo:marker marker-class-name="note">
       	*Severity
    	</fo:marker>
      </fo:block>
      
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="7%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="43%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="7%"
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
						<fo:block font-weight="bold">SEV*</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Links</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
				<xsl:choose>
					<!-- Checks whether some accidents are defined -->
					<xsl:when test="hazacc/accidents/accident">
						<xsl:for-each select="hazacc/accidents/accident">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
           						<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block >
										<xsl:value-of select="idString" />
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
										<xsl:value-of select="@severityType" />
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
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Hazards
    	</fo:marker>
      	<fo:marker marker-class-name="note">
       	*Severity
    	</fo:marker>
      </fo:block>
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="25%"
			border-style="none" />
			<fo:table-column column-number="3" column-width="40%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="15%"
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
						<fo:block font-weight="bold">SEV*</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Links</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="hazacc/hazards/hazard">
						<xsl:for-each select="hazacc/hazards/hazard">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="idString" />
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
										<xsl:value-of select="@severityType" />
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
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Safety Constraints
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="13%"
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
						<fo:block font-weight="bold">Safety Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Links</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
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
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="idString" />
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
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="links" />
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
	
	<!-- ################### Causal Safety Constraints Table ################### -->
	<xsl:template name="causalSafetyConstraintsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Safety Constraints Step 4
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="13%"
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
						<fo:block font-weight="bold">Safety Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Links</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
				<xsl:choose>
					<!-- Checks whether some Safety Constraints are defined -->
					<xsl:when test="sds/safetyConstraints/safetyConstraint">
						<xsl:for-each select="causalfactor/causalSafetyConstraints/causalSafetyConstraint">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
           			<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="idString" />
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
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="links" />
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
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	System Goals
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
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
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="idString" />
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
	
	<!-- ################### Design Requirements Table ################### -->
	<xsl:template name="designRequirementsTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Design Requirements
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="13%"
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
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Links</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
	
		<fo:table-body>
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
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
							<fo:table-cell padding="3px">
								<fo:block>
										<xsl:value-of select="idString" />
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
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="links" />
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
	
	<!-- ################### Design Requirements Step 1 Table ################### -->
	<xsl:template name="designRequirementsStep1Table">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Design Requirements Step 3
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="13%"
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
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Links</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
	
		<fo:table-body>
			<xsl:choose>
				<!-- Checks whether some Design Requirements are defined -->
				<xsl:when test="sds/designRequirementsStep1/designRequirement">
					<xsl:for-each select="sds/designRequirementsStep1/designRequirement">
						<fo:table-row border="none">
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
							</xsl:if>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
							<fo:table-cell padding="3px">
								<fo:block>
										<xsl:value-of select="idString" />
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
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="links" />
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
	
	<!-- ################### Design Requirements Step 2 Table ################### -->
	<xsl:template name="designRequirementsStep2Table">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Design Requirements Step 4
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="7%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="30%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="50%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="13%"
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
				<fo:table-cell padding="3px">
					<fo:block font-weight="bold">Links</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
	
		<fo:table-body>
			<xsl:choose>
				<!-- Checks whether some Design Requirements are defined -->
				<xsl:when test="sds/designRequirementsStep2/designRequirement">
					<xsl:for-each select="sds/designRequirementsStep2/designRequirement">
						<fo:table-row border="none">
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
							</xsl:if>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
							<fo:table-cell padding="3px">
								<fo:block>
										<xsl:value-of select="idString" />
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
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="links" />
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
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Control Actions
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
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
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Id</fo:block>
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
				<xsl:choose>
					<!-- Checks whether there are some Control Actions defined -->
					<xsl:when test="cac/controlactions/controlaction">
						<xsl:for-each select="cac/controlactions/controlaction">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
            <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="idString" />
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
      <fo:block>
      	<fo:marker marker-class-name="header_value">
        Corresponding Safety Constraints
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="10%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="1" column-width="10%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="35%"
				border-style="none" />
			<fo:table-column column-number="1" column-width="15%"
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
						   Unsafe Control Actions
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
						Corresponding Safety Constraints
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">
                                 <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
						Links
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:choose>
					<!-- Checks for hazardous UCA -->
					<xsl:when
						test="cac/controlactions/controlaction/unsafecontrolactions/unsafecontrolaction[links != 'Not Hazardous']">
						<xsl:for-each
							select="cac/controlactions/controlaction/unsafecontrolactions/unsafecontrolaction[links != 'Not Hazardous']">
	
								<fo:table-row border="none">
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="idString" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="description" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="correspondingSafetyConstraint/idString" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="correspondingSafetyConstraint/text" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="correspondingSafetyConstraint/links" />
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
	
	<!-- ################### Causal Factors Table ################### -->
	<xsl:template name="causalFactorsTable">
	
      <xsl:param name="useScenarios" >
				<xsl:value-of select="causalfactor/@useScenarios"/>
				</xsl:param>
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Causal Factors Table
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
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
													<xsl:choose>
													<xsl:when test="$useScenarios = 'true'">
														<fo:table-column column-number="1" column-width="20%"/>
														<fo:table-column column-number="2" column-width="20%"/>
														<fo:table-column column-number="3" column-width="20%"/>
														<fo:table-column column-number="4" column-width="20%"/>
														<fo:table-column column-number="5" column-width="20%" border-style="none" />
													</xsl:when>
													<xsl:otherwise>
														<fo:table-column column-number="1" column-width="25%"/>
														<fo:table-column column-number="2" column-width="10%"/>
														<fo:table-column column-number="3" column-width="20%"/>
                                                        <fo:table-column column-number="4" column-width="20%"/>
                                                        <fo:table-column column-number="5" column-width="10%"/>
														<fo:table-column column-number="6" column-width="15%" border-style="none" />
													</xsl:otherwise>
													</xsl:choose>
						
													<fo:table-header border="none" background-color="#1A277A"
														color="#FFFFFF">
														<!-- Sets the PDF-Theme-Color -->
														<xsl:call-template name="headTheme"/>
														<xsl:call-template name="fontTheme"/>
														<fo:table-row>
           													<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
															<fo:table-cell>
																<fo:block font-weight="bold">Unsafe Control Action</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block font-weight="bold">Hazard Links</fo:block>
															</fo:table-cell>
															<xsl:choose>
																<xsl:when test="$useScenarios = 'true'">
																	<fo:table-cell>
																		<fo:block font-weight="bold">Scenario</fo:block>
																	</fo:table-cell>
																</xsl:when>
															</xsl:choose>
															<fo:table-cell>
																<fo:block font-weight="bold">Safety Constraint</fo:block>
															</fo:table-cell>
                                                            <xsl:choose>
                                                                <xsl:when test="$useScenarios = 'false'">
                                                                    <fo:table-cell>
                                                                        <fo:block font-weight="bold">Design hints</fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell>
                                                                          <fo:block>Links</fo:block>
                                                                    </fo:table-cell>
                                                                </xsl:when>
                                                            </xsl:choose>
															<fo:table-cell>
																<fo:block font-weight="bold">Notes</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-header>
													<fo:table-body>
														<!-- Empty Table-Body -->
														<fo:table-row>
															<xsl:choose>
																<xsl:when test="$useScenarios = 'true'">
																	<fo:table-cell>
																		<fo:block/>
																	</fo:table-cell>
																</xsl:when>
															</xsl:choose>
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
				<xsl:choose>
				<!-- Checks if there are some components for the CausalFactors-Table -->
					<xsl:when test="causalfactor/componentsList/component/causalFactors">
						<xsl:for-each select="causalfactor/componentsList/component">
							<xsl:choose>
								<xsl:when test="causalFactors">
									<fo:table-row border-bottom="2pt solid black"
										border-top="2pt solid black">
										<fo:table-cell padding="4px" background-color="#FFFFFF"
											color="#000000" border-right="2pt solid black">
											<fo:block  font-weight="bold">
          									 	<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
												<xsl:value-of select="title" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
													<!-- ***** CausalFactors with its relatives ***** -->
													<fo:block >
													<xsl:call-template name="causalFactorSubTable"> 
						                            	<xsl:with-param name="useScenarios" select="$useScenarios" />
						                            	<xsl:with-param name="varSize" select="$varSize" />
						                           	</xsl:call-template>
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
      <xsl:param name="useScenarios" select="false"/> 
      <xsl:param name="varSize" select="12"/> 
		<fo:table>
			<fo:table-column column-number="1" column-width="15%"
				border-style="solid" />
			<fo:table-column column-number="2" column-width="85%"
				border-style="solid" />
			<fo:table-body>
			<xsl:choose>
				<xsl:when test="causalFactors/factor">
					<xsl:for-each select="causalFactors/factor">
						<fo:table-row border-bottom="2pt solid black">
           				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
							<fo:table-cell>
								<fo:block page-break-inside="avoid">
									<xsl:value-of select="text" />
								</fo:block>
							</fo:table-cell>	
							<fo:table-cell padding="0px">
								<fo:block >
									<xsl:call-template name="causalFactorRelatives">
				                            	<xsl:with-param name="useScenarios" select="$useScenarios" />
				                           	</xsl:call-template>
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
      <xsl:param name="useScenarios" select="false"/> 
    <fo:table>
		<xsl:choose>
		<xsl:when test="$useScenarios = 'true'">
			<fo:table-column column-number="1" column-width="20%"/>
			<fo:table-column column-number="2" column-width="20%"/>
			<fo:table-column column-number="3" column-width="40%"/>
			<fo:table-column column-number="4" column-width="20%" border-style="none" />
		</xsl:when>
		<xsl:otherwise>
			<fo:table-column column-number="1" column-width="25%"/>
			<fo:table-column column-number="2" column-width="60%"/>
			<fo:table-column column-number="3" column-width="15%" border-style="none" />
		</xsl:otherwise>
		</xsl:choose>
			<fo:table-body>
				<xsl:choose>
					<!-- Checks if there are some CausalFactors defined -->
					<!-- for a component. -->
					<xsl:when test="causalEntries/causalEntry">
					<xsl:for-each select="causalEntries/causalEntry">
						<fo:table-row>
							<!-- Sets the row-colour -->
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
							</xsl:if>
							<!-- Causal Factor -->
							<fo:table-cell padding="3px">
								<fo:block page-break-inside="avoid">
									<xsl:value-of select="ucaDescription" />
								</fo:block>
							</fo:table-cell>
							<!-- Safety Constraint -->
							<xsl:choose>
								<xsl:when test="$useScenarios = 'true'">
                                <!-- Hazard Links -->
                                    <fo:table-cell padding="4px">
                                        <fo:block>
                                        <xsl:value-of select="hazardLinks"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="3px">
									   <xsl:call-template name="causalFactorScenarioTable"/>
                                    </fo:table-cell>
								</xsl:when>
								<xsl:otherwise>
                                    <fo:table-cell padding="3px">
								    	<xsl:call-template name="causalFactorHazardEntries"/>
                                    </fo:table-cell>
								</xsl:otherwise>
							</xsl:choose>
							
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

    <xsl:template name="causalFactorHazardEntries">
        <fo:table>
            <fo:table-column column-number="1" column-width="17%"
                border-style="none" />
            <fo:table-column column-number="2" column-width="33%"
                border-style="none" />
            <fo:table-column column-number="2" column-width="33%"
                border-style="none" />
            <fo:table-column column-number="2" column-width="8%"
                border-style="none" />
            <fo:table-column column-number="2" column-width="8%"
                border-style="none" />
            <fo:table-body>
                <xsl:choose>
                    <xsl:when test="hazardEntries/entry">
                        <xsl:for-each select="hazardEntries/entry">
                            <fo:table-row border-bottom="1px solid black">
                                <fo:table-cell  padding="3px">
                                    <fo:block >
                                        <xsl:value-of select="text" />
                                    </fo:block>
                                </fo:table-cell>    
                                <fo:table-cell padding="3px">
                                    <fo:block >
                                        <xsl:value-of select="constraint"/>
                                    </fo:block>
                                </fo:table-cell>        
                                <fo:table-cell padding="3px">
                                    <fo:block >
                                        <xsl:value-of select="designHint"/>
                                    </fo:block>
                                </fo:table-cell>             
                                <fo:table-cell padding="3px">
                                    <fo:block >
                                        <xsl:value-of select="designRequirementLink"/>
                                    </fo:block>
                                </fo:table-cell>             
                                <fo:table-cell padding="3px">
                                    <fo:block >
                                        <xsl:value-of select="cscReference"/>
                                    </fo:block>
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
	
<!-- ################### Unsafe Control Actions Table (UCA) ################### -->
	<xsl:template name="ucaTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/>
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Unsafe Control Actions Table
    	</fo:marker>
      	<fo:marker marker-class-name="note">
       	*Severity
    	</fo:marker>
      </fo:block>
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
						<fo:table>
							<fo:table-column column-number="1" column-width="90%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="10%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-weight="bold">Not providing causes hazard </fo:block>
									</fo:table-cell>
									<fo:table-cell>
							            <fo:block-container reference-orientation="270">
           									<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
							              <fo:block>SEV*</fo:block>
							            </fo:block-container>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:table>
							<fo:table-column column-number="1" column-width="90%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="10%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-weight="bold">Providing causes hazard</fo:block>
									</fo:table-cell>
									<fo:table-cell>
							            <fo:block-container reference-orientation="270">
           									<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
							              <fo:block>SEV*</fo:block>
							            </fo:block-container>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:table>
							<fo:table-column column-number="1" column-width="90%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="10%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-weight="bold">Wrong timing or order causes hazard</fo:block>
									</fo:table-cell>
									<fo:table-cell>
							            <fo:block-container reference-orientation="270">
           									<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
							              <fo:block>SEV*</fo:block>
							            </fo:block-container>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:table>
							<fo:table-column column-number="1" column-width="90%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="10%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-weight="bold">Stopped too soon or applied too long</fo:block>
									</fo:table-cell>
									<fo:table-cell>
							            <fo:block-container reference-orientation="270" font-size="8pt">
							              <fo:block>SEV*</fo:block>
							            </fo:block-container>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:choose>
					<!-- Checks if there are some Control Actions already defined -->
					<xsl:when test="cac/controlactions/controlaction">
						<xsl:for-each select="cac/controlactions/controlaction">
							<fo:table-row border-bottom="2pt solid black"
								border-top="2pt solid black">
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
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
					select="unsafecontrolactions/unsafecontrolaction[type='NOT_GIVEN']">
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
				select="unsafecontrolactions/unsafecontrolaction[type='GIVEN_INCORRECTLY']">
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
					select="unsafecontrolactions/unsafecontrolaction[type='WRONG_TIMING']">
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
					select="unsafecontrolactions/unsafecontrolaction[type='STOPPED_TOO_SOON']">
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
				<fo:table-column column-width="90%" />
				<fo:table-column column-width="10%"  background-color="#fcfcfc"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:table>
								<fo:table-column column-width="100%" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block color="#820000">
												<xsl:value-of select="idString" />
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
						</fo:table-cell>
						<fo:table-cell>
							<fo:block color="#820000">
								<xsl:value-of select="@severityType" />
							</fo:block>
						</fo:table-cell>
					
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
    
    <xsl:template name="glossary">
      <fo:block>
      	<fo:marker marker-class-name="header_value">
       	Glossary
    	</fo:marker>
      	<fo:marker marker-class-name="note">
    	</fo:marker>
      </fo:block>
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
        ACCIDENT
    </fo:block>
    <fo:block>
    undesired or unplanned event that results in a loss, including loss or injury to
    human life, property damage, environmental pollution, mission loss etc.
    </fo:block>
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    ACTUATOR
    </fo:block>
    a human operator or mechanical device tasked with directly acting upon a process
    and changing its physical state. Valve systems (valve + the motor associated to it), doors,
    magnets (their electronic controller and power source included) or a nurse are actuators that
    respectively implement control on the following processes: "fluid flow", "egress availability",
    "beam position", "patient position". Actuators, like sensors, can be smart in that they can be
    programmable; they may therefore need to be studied with the same concepts as the controllers
    are.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">    CAUSAL FACTOR
    </fo:block>
    cause of a (hazardous) scenario (STPA Step 2).
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    COMMAND
    </fo:block>
    a signal providing a set of instructions (goals, set points, order) issued by a
    controller with the intent of acting upon a process by activation of a device or implementation of
    a procedure. Communication and Control, along with Hierarchy and Emergence, are
    fundamental systems theory concepts at the foundation of STAMP. Commands are issued by
    Controllers, with the intent that they be implemented by Actuators to act on the Controlled
    Process
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    CONTROL ACTION
    </fo:block>
    the bringing about of an alteration in the system's state through activation
    of a device or implementation of a procedure with the intent of regulating or guiding the
    operation of a human being, machine, apparatus, or system. They are the result of an Actuator
    implementing a control Command issued by a Controller, and aim at controlling the state of the
    Controlled Process
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    CONTROL STRUCTURE
    </fo:block>
    hierarchy of process loops created to steer a system's operations and
    control its states. In the context of a hazard analysis, we are most concerned with the control of
    hazardous states aimed at eliminating, reducing or mitigating them.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    CONTROLLED PROCESS
    </fo:block>
    although at times reducible to the state of a physical element (e.g.
    framing a "door" as a controlled process whose values can be "open" or "shut"), it appears
    fruitful to rather consider the controlled process identified in STAMP process loops to be the
    system's attribute or state variable that the controller aims to control (e.g. thinking of the door
    not as the controlled process but, together with its motor, as an actuator that implements control
    on the possibility of egress).
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    CONTROLLER
    </fo:block>
    a human or automated system that is responsible for controlling the system's
    processes by issuing commands to be implemented by system actuators.
    FEEDBACK
    
    evaluative or corrective information about an action, event, or process that is
    transmitted to the original or controlling source.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    HAZARD
    </fo:block>
    system state of set or conditions that, together with a particular set of worst-case
    environmental conditions, will lead to an accident.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    LOSS
    </fo:block>
    decrease in amount, magnitude or degree including destruction or ruin.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    SAFETY
    </fo:block>
    freedom from loss.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    SAFETY CONSTRAINT
    </fo:block>
    bound set on system design options and operations to restrict, compel
    to avoid or forbid the performance of actions that would lead to a hazard.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    SAFETY/DESIGN REQUIREMENT
    </fo:block>
    design requirement formulated to include the enforcement of safety
    constraints as a design objective.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    (HAZARDOUS) SCENARIO
    </fo:block>
    an account or synopsis of a possible course of action or events
    resulting in a hazard. See Causal Factor.
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    SENSOR
    </fo:block>
    human or mechanical device tasked with measuring a process variable by responding
    to a physical stimulus (as heat, light, sound, pressure, magnetism, or a particular motion) and
    transmit a resulting impulse (as for measurement or operating a control).
    <fo:block space-before="10pt" font-size="9pt" page-break-after="avoid">
    UNSAFE CONTROL ACTION
    </fo:block>
    control action that leads to a hazard (STPA Step 1).
    <fo:block space-before="15pt" font-size="8pt" page-break-after="avoid">
    Definitions from:
    </fo:block>
    <fo:block>
        <xsl:attribute name="font-size">9pt</xsl:attribute>
    Antoine, B. (2013). Systems Theoretic Hazard Analysis (STPA) applied to the risk review of complex systems: an example from the medical device industry (Doctoral dissertation, Massachusetts Institute of Technology).
    </fo:block>
    </xsl:template>
</xsl:stylesheet>
