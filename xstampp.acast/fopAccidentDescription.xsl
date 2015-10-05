<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="no" />

	<xsl:import href="ucaTableTemp.xsl" />
	<xsl:template match="/*">
		<fo:root>
			<!-- Page layout -->
			<fo:layout-master-set>

				<!-- Page-Master for the front-page -->
				<fo:simple-page-master margin="5mm 5mm 10mm 5mm"
					page-height="297mm" page-width="210mm" master-name="A4">
					<fo:region-body margin="15mm 0mm 10mm 5mm" />
					<fo:region-before extent="20mm" display-align="before" />
					<fo:region-after extent="10mm" display-align="after" />
				</fo:simple-page-master>

				<!-- Page-Master for the common pages -->
				<fo:simple-page-master margin="5mm 5mm 5mm 5mm"
					page-height="297mm" page-width="210mm" master-name="titel">
					<fo:region-body margin="5mm 0mm 10mm 5mm" />
				</fo:simple-page-master>

			</fo:layout-master-set>

			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++ START OF PDF ++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

			<fo:page-sequence master-reference="titel">
				<fo:flow flow-name="xsl-region-body">
					<!-- Main-data for the front-page -->
					<fo:block intrusion-displace="line">
						<fo:table keep-together.within-column="always"
							space-after="30pt" margin="5mm 5mm 10mm 5mm">
							<fo:table-column column-number="1" column-width="25%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="75%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Accident
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="projectdata/projectName" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Accident Date
											and Time
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="projectdata/accidentDate" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Accident
											Company
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="projectdata/accidentCompany" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Accident
											Description
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:call-template name="intersperse-with-zero-spaces">
												<xsl:with-param name="str"
													select="projectdata/accidentDescription" />
											</xsl:call-template>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>



			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			<!-- +++++++++++++++++++++ END OF PDF +++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

		</fo:root>
	</xsl:template>

	<xsl:template match="text()">
		<xsl:call-template name="intersperse-with-zero-spaces">
			<xsl:with-param name="str" select="." />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="intersperse-with-zero-spaces">
		<xsl:param name="str" />
		<xsl:variable name="spacechars">
			&#x9;&#xA;
			&#x2000;&#x2001;&#x2002;&#x2003;&#x2004;&#x2005;
			&#x2006;&#x2007;&#x2008;&#x2009;&#x200A;&#x200B;
		</xsl:variable>
		<xsl:if test="string-length($str) &gt; 0">
			<xsl:variable name="c1" select="substring($str, 1, 1)" />
			<xsl:variable name="c2" select="substring($str, 2, 1)" />

			<xsl:value-of select="$c1" />
			<xsl:if
				test="$c2 != '' and
        not(contains($spacechars, $c1) or
        contains($spacechars, $c2))">
				<xsl:text>&#x200B;</xsl:text>
			</xsl:if>

			<xsl:call-template name="intersperse-with-zero-spaces">
				<xsl:with-param name="str" select="substring($str, 2)" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>


