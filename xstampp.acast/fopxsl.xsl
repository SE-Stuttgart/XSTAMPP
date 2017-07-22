<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<!-- Note: This xsl gathers all the informations from the .acc-file -->
	<!-- which is generated by the "prepareForExport()"-Command in the -->
	<!-- ViewContainer.java -->
	<xsl:import href="tableTemp.xsl" />
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

			<!-- +++++ Front-Page +++++ -->
			<fo:page-sequence master-reference="titel">
				<fo:flow flow-name="xsl-region-body">
					<fo:block intrusion-displace="line">
						<fo:table space-after="30pt">
							<fo:table-column column-number="1" column-width="100%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding="10px" text-align="center">
										<fo:block font-size="18pt">
											A-CAST-Report
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell background-color="#1A277A"
										padding="10px" text-align="center">
										<xsl:call-template name="headTheme" />
										<fo:block font-size="24pt" color="#FFFFFF">
											<xsl:call-template name="fontTheme" />
											<xsl:value-of select="projectdata/projectName" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="exportinformation/logoPath">
									<fo:table-row>
										<fo:table-cell padding="15px" text-align="center">
											<fo:block>
												<fo:external-graphic content-width="100mm">
													<xsl:attribute name="src">
												<xsl:value-of select="exportinformation/logoPath" />
												</xsl:attribute>
												</fo:external-graphic>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Main-data for the front-page -->
					<fo:block intrusion-displace="line">
						<fo:table space-after="30pt" margin="5mm 5mm 10mm 5mm">
							<fo:table-column column-number="1" column-width="25%"
								border-style="none" />
							<fo:table-column column-number="2" column-width="75%"
								border-style="none" />
							<fo:table-body>
								<fo:table-row border="1pt solid black">
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt" font-weight="bold">
											Title
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
											Date and Time
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding="4px">
										<fo:block font-size="12pt">
											<xsl:value-of select="exportinformation/date" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="exportinformation/company">
									<fo:table-row border="1pt solid black">
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt" font-weight="bold">
												Company
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="4px">
											<fo:block font-size="12pt">
												<xsl:value-of select="exportinformation/company" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>

			<!-- +++++ Common Page content +++++ -->
			<fo:page-sequence master-reference="A4" id="total">
				<!-- Header-Block -->
				<fo:static-content flow-name="xsl-region-before">
					<fo:table background-color="#DEDEDE">
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding="4px">
									<fo:block font-size="10pt">
										<xsl:value-of select="projectdata/projectName" />
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
				</fo:static-content>

				<!-- Footer-Block -->
				<fo:static-content flow-name="xsl-region-after">
					<fo:table>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell background-color="#DEDEDE" padding="4px">
									<!-- Page Numbering -->
									<fo:block font-size="10pt" text-align="center">
										Page &#x0020;
										<fo:page-number />
										of
										<fo:page-number-citation-last
											ref-id="total" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding="2px">
									<fo:block font-size="8pt">Created with A-CAST</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<!-- *************** Accidents *************** -->
					<fo:block>
						<fo:block font-size="24pt" space-after="5pt"
							page-break-after="avoid">
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							Accident Description
						</fo:block>
						<fo:block intrusion-displace="line">
							<fo:table space-after="30pt" margin="5mm 5mm 10mm 5mm">
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
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="projectdata/projectName" />
												</xsl:call-template>
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
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="projectdata/accidentDate" />
												</xsl:call-template>
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
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="projectdata/accidentCompany" />
												</xsl:call-template>
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
					</fo:block>


					<!-- *************** Hazard table *************** -->
					<fo:block>
						<fo:block font-size="24pt" space-after="5pt"
							page-break-after="avoid">
							Hazards
						</fo:block>
						<!-- Hazard-Table-Template -->
						<xsl:call-template name="hazardTable" />
					</fo:block>

					<!-- *************** Safety Constraints table *************** -->
					<fo:block>
						<fo:block font-size="24pt" space-after="5pt"
							page-break-after="avoid">
							Safety Constraints
						</fo:block>
						<!-- Safety Constraint-Table-Template -->
						<xsl:call-template name="safetyConstraintsTable" />
					</fo:block>

					<!-- *************** Proximal Events table *************** -->
					<fo:block>
						<fo:block font-size="24pt" space-after="5pt"
							page-break-after="avoid">
							Proximal Events
						</fo:block>
						<!-- System Goals-Table-Template -->
						<xsl:call-template name="proxEventsTable" />
					</fo:block>



					<!-- *************** Control Structure Diagram *************** -->
					<fo:block page-break-after="always">
						<fo:block font-size="24pt" space-after="5pt"
							page-break-after="avoid">
							Control Structure Diagram
						</fo:block>
						<fo:block>
							<xsl:if test="exportinformation/csImagePath">
								<xsl:choose>
									<xsl:when
										test="exportinformation/csImageWidth &gt;= 600 or exportinformation/csImageHeight &gt;= 701">
										<fo:external-graphic
											inline-progression-dimension.maximum="90%" content-height="scale-down-to-fit"
											content-width="scale-down-to-fit">
											<xsl:attribute name="src">
									<!-- Path of the Control Structure via acc-file -->
									<xsl:value-of select="exportinformation/csImagePath" />
								</xsl:attribute>
										</fo:external-graphic>
									</xsl:when>
									<xsl:otherwise>
										<fo:external-graphic
											inline-progression-dimension.maximum="90%" content-height="75%"
											content-width="75%">
											<xsl:attribute name="src">
										<!-- Path of the Control Structure via acc-file -->
										<xsl:value-of select="exportinformation/csImagePath" />
								</xsl:attribute>
										</fo:external-graphic>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</fo:block>
					</fo:block>


					<!-- *************** Responsibilities table *************** -->
					<fo:block page-break-after="always">
						<xsl:call-template name="respTable" />
					</fo:block>

					<!-- *************** Recommendations Table *************** -->
					<fo:block page-break-after="always">

						<xsl:call-template name="recommendationTable" />
					</fo:block>

				</fo:flow>
			</fo:page-sequence>

			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
			<!-- +++++++++++++++++++++ END OF PDF +++++++++++++++++++++ -->
			<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

		</fo:root>
	</xsl:template>



</xsl:stylesheet>

