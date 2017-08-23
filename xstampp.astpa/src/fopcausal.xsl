<?xml version="1.0" encoding="ISO-8859-1"?>
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
<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- author: Lukas Balzer -->

	<xsl:import href="ucaTableTemp.xsl" />
	<xsl:param name="title.size" select="24" />
	<xsl:param name="table.head.size" select="14" />
	<xsl:param name="text.size" select="12" />
	<xsl:param name="header.omit" select="false" />
	<xsl:param name="page.layout" select="A4" />
	<xsl:param name="page.title" select="''" />

	<xsl:template match="/*">
		<fo:root>
			<!-- Page layout -->
			<fo:layout-master-set>
				<xsl:call-template name="layout" />
			</fo:layout-master-set>
			<fo:page-sequence white-space-collapse="true" id="total">
				<xsl:attribute name="master-reference"><xsl:value-of select="$page.layout" /></xsl:attribute>
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="astpaHead">
						<xsl:with-param name="pdfTitle" select="$page.title" />
					</xsl:call-template>
				</fo:static-content>

				<!-- Footer-Block -->
				<fo:static-content flow-name="xsl-region-after">
					<xsl:call-template name="astpaFooter" />
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">

					<!-- *************** Causal Factors-Table *************** -->
					<fo:block>
						<fo:block space-after="5pt" page-break-after="avoid">
							<xsl:attribute name="font-size"><xsl:value-of
								select="$title.size" />pt</xsl:attribute>
							Causal Factors Table
						</fo:block>
						<!-- Causal Factors-Table-Template -->
						<xsl:call-template name="causalFactorsTable">
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
