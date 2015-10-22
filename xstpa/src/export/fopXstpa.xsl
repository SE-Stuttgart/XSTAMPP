<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- author: Yannic Sowoidnich -->
    <xsl:import href="src/export/xstpaTemp.xsl"/>
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
					
                  <!-- ################### Refined Safety Requirements-Table ################### -->
					<fo:block>
						<fo:block space-after="5pt" page-break-after="avoid">
						   <xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
	                     Refined Safety Requirements
	                  	</fo:block>
	                  	<xsl:call-template name="refinedSafetyTable">
	            	        <xsl:with-param name="varSize" select="$text.size" />
	                        <xsl:with-param name="headSize" select="$table.head.size" />
	                        <xsl:with-param name="omitHeader" select="$header.omit" />
	                	</xsl:call-template>
                        
					</fo:block>
					
					<!-- ################### Context Table Provided ################### -->
					<fo:block>
						<fo:block space-after="5pt" page-break-after="avoid">
						   <xsl:attribute name="font-size"><xsl:value-of select="$title.size" />pt</xsl:attribute>
	                     Refined Safety Requirements
	                  	</fo:block>
	                  	<xsl:call-template name="contextTableProvided">
	            	        <xsl:with-param name="varSize" select="$text.size" />
	                        <xsl:with-param name="headSize" select="$table.head.size" />
	                        <xsl:with-param name="omitHeader" select="$header.omit" />
	                	</xsl:call-template>
                        
					</fo:block>
        
        
					
				</fo:flow>
			</fo:page-sequence>
        </fo:root>
	</xsl:template>
    
    
    
</xsl:stylesheet>