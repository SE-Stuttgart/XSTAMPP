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
					
					<!-- *************** Template *************** -->
					<fo:block wrap-option="wrap">
						<fo:block space-after="5pt" page-break-after="avoid">
                  		<xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
							<xsl:value-of select="$page.title" />
						</fo:block>
						<!-- Causal Factors-Table-Template -->
						<xsl:call-template name="ltlTemplate">
                            <xsl:with-param name="varSize" select="$text.size" />
                            <xsl:with-param name="headSize" select="$table.head.size" />
                            <xsl:with-param name="omitHeader" select="$header.omit" />
                           </xsl:call-template>
					</fo:block>
        
        
					
				</fo:flow>
			</fo:page-sequence>
        </fo:root>
	</xsl:template>
    
    	<!-- ################### Corresponding Security Constraints Table ################### -->
	<xsl:template name="ltlTemplate">
	
      <xsl:param name="varSize" select="12"/> 
      <xsl:param name="headSize" select="14"/> 
      <xsl:param name="omitHeader" select="false"/> 
		<fo:table border="none" space-after="30pt">
           <xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="10%" border-style="none" />
			<fo:table-column column-number="2" column-width="90%" border-style="none" />
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
						   		LTL Formulas
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:if test="cac/controlactions/controlaction">
					<xsl:for-each select="cac/controlactions/controlaction">
						<xsl:choose>
							<xsl:when test="rules/rule">
								<xsl:for-each select="rules/rule">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
												   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
													PR1.<xsl:value-of select="ruleNR" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block page-break-inside="avoid">
												   <xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
													<xsl:value-of select="ltlProp" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>						
								</xsl:for-each>
							</xsl:when>
						</xsl:choose>
					</xsl:for-each>
				</xsl:if>
				
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>