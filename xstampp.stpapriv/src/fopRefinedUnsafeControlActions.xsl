<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- author: Lukas Balzer -->
    
    <xsl:import href="ucaTableTemp.xsl"/>
    <xsl:param name="title.size" select="24"/> 
    <xsl:param name="table.head.size" select="14"/> 
    <xsl:param name="text.size" select="12"/>
    <xsl:param name="header.omit" select="false"/>  
     <xsl:param name="page.layout" select="A4"/>  
     <xsl:param name="page.title" select="''"/>  
    
    <xsl:template match="/*">
    <fo:root>
        <!-- Page layout -->
	    <fo:layout-master-set>
			<xsl:call-template name="layout"/>
		</fo:layout-master-set>	
         <fo:page-sequence white-space-collapse="true" id="total">  
         	<xsl:attribute name="master-reference"><xsl:value-of select="$page.layout"/></xsl:attribute>
            	<fo:static-content flow-name="xsl-region-before">
						<xsl:call-template name="stpasecHead">
                            <xsl:with-param name="pdfTitle" select="$page.title" />
						</xsl:call-template>
				</fo:static-content>

				<!-- Footer-Block -->
				<fo:static-content flow-name="xsl-region-after">
						<xsl:call-template name="stpasecFooter"/>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					
					<!-- *************** Refined Security Constraints Table *************** -->
					<fo:block wrap-option="wrap">
						<fo:block space-after="5pt" page-break-after="avoid">
                  <xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
							<xsl:value-of select="$page.title" />
						</fo:block>
						<!-- Causal Factors-Table-Template -->
						<xsl:call-template name="refinedUCATable">
                            <xsl:with-param name="varSize" select="$text.size" />
                            <xsl:with-param name="headSize" select="$table.head.size" />
                            <xsl:with-param name="omitHeader" select="$header.omit" />
                           </xsl:call-template>
					</fo:block>
        
        
					
				</fo:flow>
			</fo:page-sequence>
        </fo:root>
	</xsl:template>
    
    	<xsl:template name="refinedUCATable">
      	<xsl:param name="varSize" select="12"/> 
     	<xsl:param name="headSize" select="14"/> 
      	<xsl:param name="omitHeader" select="false"/>       
    	<xsl:param name="value"/>
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="25%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="25%"
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
						<fo:block font-weight="bold">Wrong timing causes vulnerability </fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks if there are some Control Actions already defined -->
					<xsl:when test="cac/controlactions/controlaction">
						<xsl:for-each select="cac/controlactions/controlaction">
							
							<xsl:choose>
								<xsl:when test="rules/rule">
									<fo:table-row border-bottom="solid"
										border-top="solid" keep-together.within-column="1">
										<fo:table-cell padding="4px" background-color="#FFFFFF"
											color="#000000" border-right="2pt solid black">
											<fo:block font-weight="bold">
												<xsl:value-of select="title" />
											</fo:block>
										</fo:table-cell>
										
										<!-- Creates for each Control Action a Unsafe Control Action Cell -->
										
										<!-- NOT PROVIDED-Cell -->
										<fo:table-cell padding="4px" background-color="#FFFFFF">
											<!-- NOT GIVEN - SubTable -->
											<xsl:call-template name="notProvided"/>
										</fo:table-cell>
					
										<!-- PROVIDED-Cell -->
										<fo:table-cell padding="4px" background-color="#FFFFFF">
											<!-- GIVEN_INCORRECTLY - SubTable -->
											<xsl:call-template name="provided"/>
										</fo:table-cell>
					
										<!-- ########## WRONG TIMING -Cell ########## -->
										<fo:table-cell padding="4px" background-color="#FFFFFF">
											<!-- WRONG TIMING - SubTable -->
											<xsl:call-template name="wrongTiming"/>
										</fo:table-cell>
								
									</fo:table-row>
								</xsl:when>
							</xsl:choose>
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
							</fo:table-row>
						</xsl:otherwise>
					</xsl:choose>
				</fo:table-body>
			</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: NOT GIVEN ################### -->
	<xsl:template name="notProvided">
		<fo:table>
			<fo:table-column column-width="100%" />
			<fo:table-body>
				<!-- empty row -->
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>	
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="rules/rule[type='not provided']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
							<fo:block page-break-inside="avoid">
								<xsl:call-template name="rucaEntry"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ################### UCA-Sub-Table- type: provided ################### -->
	<xsl:template name="provided">
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
					select="rules/rule[type='provided']">
					<fo:table-row>
						<!-- Sets the row-colour -->
						<xsl:if test="position() mod 2 = 0">
							<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
						</xsl:if>
						<fo:table-cell padding="3px">
							<fo:block page-break-inside="avoid">
								<xsl:call-template name="rucaEntry"/>
							</fo:block>
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
				<xsl:for-each select="rules/rule">
					<xsl:if test="type='provided too late' or type='provided too early'">
						<fo:table-row>
							<!-- Sets the row-colour -->
							<xsl:if test="position() mod 2 = 0">
								<xsl:attribute name="background-color">
						#D9D9D9
						</xsl:attribute>
							</xsl:if>
							<fo:table-cell padding="3px">
								<fo:block page-break-inside="avoid">
									<xsl:call-template name="rucaEntry"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	
	<!-- ################### RUCA - Hazards ################### -->
	<xsl:template name="rucaEntry">
		<fo:table>
				<fo:table-column column-width="100%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block color="#820000">
								RPR1.<xsl:value-of select="ruleNR" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell padding="3px">
							<fo:block>
								<xsl:value-of select="refinedUCA" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<xsl:choose>
								<xsl:when test="links">
									<fo:block color="#820000">
										&#x005B;
										<xsl:value-of select="links" />
										&#x005D;
									</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block/>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
	</xsl:template>
</xsl:stylesheet>