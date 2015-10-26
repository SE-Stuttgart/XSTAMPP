<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
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

<!-- ################### Refined Safety Requirements-Table ################### -->
	<xsl:template name="refinedSafetyTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" column-width="5%"
			border-style="none" />
		<fo:table-column column-number="2" column-width="12%"
			border-style="none" />
		<fo:table-column column-number="3" column-width="15%"
			border-style="none" />
		<fo:table-column column-number="4" column-width="45%"
			border-style="none" />
		<fo:table-column column-number="5" column-width="23%"
			border-style="none" />
		
		<fo:table-header border="none" background-color="#1A277A"
			color="#FFFFFF" padding="3px">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<!--<xsl:call-template name="headTheme"/>-->
				<!--<xsl:call-template name="fontTheme"/>-->
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Actions</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Context</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Hazardous Combinations</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Refined Safety Requirements</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some Combinations are defined -->
					<xsl:when test="providedca/controlactions/contexttablecombinations/contexttablecombination/hAnytime = 'true' or 
					notprovidedca/controlactions/contexttablecombinations/contexttablecombination/hazardous = 'true'">
						<xsl:for-each select="providedca/controlactions/contexttablecombinations/contexttablecombination[hAnytime = 'true'] |
						 notprovidedca/controlactions/contexttablecombinations/contexttablecombination[hazardous = 'true']">
							
								<fo:table-row border="none">
								
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="background-color">
											#D9D9D9
										</xsl:attribute>
									</xsl:if>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="position()" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="linkedControlActionName" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="context" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:for-each select="pmValues/pmValue">
												<xsl:value-of select="."/>
												
												<xsl:if test="position() != last()">
													<xsl:text>, </xsl:text>
												</xsl:if>
												<xsl:if test="position() mod 2 = 0">
													<fo:block/>
												</xsl:if>																											
											</xsl:for-each>									
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="refinedSafetyRequirements" />
										</fo:block>
									</fo:table-cell>
								
								</fo:table-row>		
									
						</xsl:for-each>
					</xsl:when>

					<!-- If there are no requirements defined the first row -->
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
	
	<!-- ################### Context Table Provided!################### -->
	<xsl:template name="contextTableProvided">
    	<xsl:param name="varSize" select="12"/> 
    	<xsl:param name="headSize" select="14"/> 
    	<xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
        	<xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
        	
			<fo:table-column column-number="1" border-style="none" />
			<fo:table-column column-number="2" border-style="none" />
        	<xsl:for-each select="tableHeaders/tableHeader">
        		<fo:table-column column-number="position()+2" border-style="none" />
        	</xsl:for-each>

        	
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
           	<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>				
				<fo:table-row>
				
					<fo:table-cell padding="3px">
							<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<xsl:for-each select="tableHeaders/tableHeader">
						<fo:table-cell padding="3px">
							<fo:block font-weight="bold"><xsl:value-of select="."/></fo:block>
						</fo:table-cell>
					</xsl:for-each>
					<fo:table-cell padding="3px">
							<fo:block font-weight="bold">Hazardous if provided<fo:block/> Anytime To Late To Early</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some Combinations are defined -->
					<xsl:when test="providedca/controlactions/contexttablecombinations/contexttablecombination">
						<xsl:for-each select="providedca/controlactions/contexttablecombinations/contexttablecombination">
							
								<fo:table-row border="none">
								
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="background-color">
											#D9D9D9
										</xsl:attribute>
									</xsl:if>
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="position()" />
										</fo:block>
									</fo:table-cell>
									<xsl:for-each select="values/value">
										<fo:table-cell padding="3px">
											<fo:block >
												<xsl:value-of select="." />
											</fo:block>
										</fo:table-cell>
									</xsl:for-each>
									<fo:table-cell padding="3px">
										<fo:block >
											<xsl:value-of select="hAnytime" />,
											<xsl:value-of select="hLate" />,
											<xsl:value-of select="hEarly" />
										</fo:block>
									</fo:table-cell>
								
								</fo:table-row>		
									
						</xsl:for-each>
					</xsl:when>

					<!-- If there are no requirements defined the first row -->
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
	
</xsl:stylesheet>