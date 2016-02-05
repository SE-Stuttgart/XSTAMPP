<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- author: Lukas Balzer -->
    
    <xsl:import href="ucaTableTemp.xsl"/>
    <xsl:param name="title.size" select="24"/> 
    <xsl:param name="table.head.size" select="14"/> 
    <xsl:param name="text.size" select="12"/>
    <xsl:param name="header.omit" select="false"/>   
    
    <xsl:template match="/*">
    <fo:root>
        <!-- Page layout -->
        <fo:layout-master-set>

            <fo:simple-page-master master-name="HelloWorld" page-width="auto" page-height="auto">    
                <fo:region-body/>                               
            </fo:simple-page-master>
        </fo:layout-master-set>
         <fo:page-sequence master-reference="HelloWorld" white-space-collapse="true">  
            <fo:flow flow-name="xsl-region-body">
				
				<!-- *************** System Goals-Table *************** -->
				<fo:block>
					<xsl:for-each select="cac/controlactions/controlaction[isSafetyCritical]">
						<xsl:choose>
							<xsl:when test="dependenciesForProvided/variableName">
								<fo:block space-after="5pt" page-break-after="avoid">
				               
									<xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
									   Context Table for <xsl:value-of name="title"/> Provided
								</fo:block>
								<!-- ################### Context Table Template ################### -->
								<xsl:call-template name="contextProvidedTable">
								          <xsl:with-param name="varSize" select="$text.size" />
								          <xsl:with-param name="headSize" select="$table.head.size" />
								          <xsl:with-param name="omitHeader" select="$header.omit" />
								</xsl:call-template>    
							</xsl:when>
						</xsl:choose>
	                </xsl:for-each>        
               </fo:block>
        
        
               
            </fo:flow>
         </fo:page-sequence>
        </fo:root>
   </xsl:template>
    
    
    <!-- ################### Context-Table ################### -->
	<xsl:template name="contextProvidedTable">
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
	<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
		<fo:table-column column-number="1" 	border-style="solid" />
		<fo:table-column column-number="2" border-style="solid" />
		<fo:table-column column-number="3" 	border-style="solid" />
		<xsl:for-each select="dependenciesForProvided/variableName">
			<fo:table-column column-number="position() + 3" border-style="solid" />
      		<xsl:param name="columns" select="position() + 3"/>
		</xsl:for-each>
		
		
		<fo:table-header border="solid" background-color="#1A277A"
			color="#FFFFFF" padding="3px">
           <xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
			<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme"/>
				<xsl:call-template name="fontTheme"/>
				
				<fo:table-row>
					<fo:table-cell padding="3px">
						<xsl:attribute name="number-columns-spanned"><xsl:value-of select="$columns" /></xsl:attribute>
						<fo:block font-weight="bold"></fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px" number-columns-spanned="3">
						<fo:block text-align="center" font-weight="bold">Hazardous control action</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<xsl:for-each select="dependenciesForProvided">
						<fo:table-cell padding="3px">
							<fo:block font-weight="bold"><xsl:value-of select="variableName" /></fo:block>
						</fo:table-cell>
					</xsl:for-each>
					<fo:table-cell padding="3px">
					<fo:block font-weight="bold">If provided any time in this context</fo:block>
					</fo:table-cell>
						<fo:table-cell padding="3px">
						<fo:block font-weight="bold">If provided too early in this context</fo:block>
					</fo:table-cell>
						<fo:table-cell padding="3px">
						<fo:block font-weight="bold">If provided too late in this context</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
		
			<fo:table-body>
           <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="PMCombisWhenProvided/combinationOfPMValues">
						<xsl:for-each select="PMCombisWhenProvided/combinationOfPMValues">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="valueNames">
									<fo:table-cell padding="3px">
										<fo:block font-weight="bold"><xsl:value-of select="name" /></fo:block>
									</fo:table-cell>
								</xsl:for-each>
								<xsl:choose>
									<xsl:when test="hazardousAnyTime">
										<fo:table-cell padding="3px">
											<fo:block>Yes</fo:block>
										</fo:table-cell>
									</xsl:when>
									<xsl:otherwise>
										<fo:table-cell padding="3px">
											<fo:block>No</fo:block>
										</fo:table-cell>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="hazardousifProvidedToEarly">
										<fo:table-cell padding="3px">
											<fo:block>Yes</fo:block>
										</fo:table-cell>
									</xsl:when>
									<xsl:otherwise>
										<fo:table-cell padding="3px">
											<fo:block>No</fo:block>
										</fo:table-cell>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="hazardousToLate">
										<fo:table-cell padding="3px">
											<fo:block>Yes</fo:block>
										</fo:table-cell>
									</xsl:when>
									<xsl:otherwise>
										<fo:table-cell padding="3px">
											<fo:block>No</fo:block>
										</fo:table-cell>
									</xsl:otherwise>
								</xsl:choose>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no hazards defined the first row -->
					<!-- should be filled with strokes -->
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>