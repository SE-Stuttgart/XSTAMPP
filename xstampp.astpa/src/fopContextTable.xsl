<?xml version="1.0" encoding="ISO-8859-1"?>

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

					<!-- *************** Context Table Template *************** -->
					<fo:block>
						<xsl:variable name="contentSize">
							<xsl:attribute name="select">7</xsl:attribute>
						</xsl:variable>
						<xsl:for-each
							select="cac/controlactions/controlaction[isSafetyCritical = 'true']">
							<xsl:choose>
								<!-- ################### Context Table Template ################### -->
								<xsl:when
									test="dependenciesForProvided/variableName and PMCombisWhenProvided/combinationOfPMValues[valueNames/name]">
									<fo:block space-after="5pt" page-break-after="avoid">

										<xsl:attribute name="font-size"><xsl:value-of
											select="$title.size" />pt</xsl:attribute>
										Context Table of control action
										<xsl:value-of select="$contentSize" />
										in context provided
									</fo:block>
									<fo:block text-align="center" page-break-after="always">
										<xsl:call-template name="contextProvidedTable">
											<xsl:with-param name="varSize" select="$text.size" />
											<xsl:with-param name="headSize" select="$table.head.size" />
											<xsl:with-param name="omitHeader" select="$header.omit" />
										</xsl:call-template>
									</fo:block>
								</xsl:when>
							</xsl:choose>
							<xsl:choose>
								<xsl:when
									test="dependenciesForNotProvided/variableName and PMCombisWhenNotProvided/combinationOfPMValues[valueNames/name]">
									<fo:block space-after="5pt" page-break-after="avoid">

										<xsl:attribute name="font-size"><xsl:value-of
											select="$title.size" />pt</xsl:attribute>
										Context Table of control action
										<xsl:value-of select="title" />
										in context not provided
									</fo:block>
									<fo:block text-align="center" page-break-after="always">
										<xsl:call-template name="contextNOTProvidedTable">
											<xsl:with-param name="varSize" select="$text.size" />
											<xsl:with-param name="headSize" select="$table.head.size" />
											<xsl:with-param name="omitHeader" select="$header.omit" />
										</xsl:call-template>
									</fo:block>
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
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="caTitle" select="title" />
		<xsl:param name="omitHeader" select="false" />
		<fo:table border="none" space-after="30pt">
			<xsl:variable name="columns"
				select="count(dependenciesForProvided/variableName)+4" />
			<xsl:variable name="variables"
				select="count(dependenciesForProvided/variableName)" />

			<xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" border-style="solid" />
			<xsl:for-each select="dependenciesForProvided/variableName">
				<fo:table-column border-style="solid" />
			</xsl:for-each>
			<fo:table-column column-width="5%" border-style="solid" />
			<fo:table-column column-width="5%" border-style="solid" />
			<fo:table-column column-width="5%" border-style="solid" />

			<fo:table-header border="solid" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
				<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme" />
				<xsl:call-template name="fontTheme" />

				<fo:table-row keep-together.within-column="always">
					<fo:table-cell padding="3px" number-rows-spanned="2">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<xsl:for-each select="dependenciesForProvided/variableName">
						<fo:table-cell padding="3px" number-rows-spanned="2">
							<fo:block wrap-option="wrap" font-weight="bold">
								<xsl:value-of select="." />
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>

					<fo:table-cell padding="3px" number-columns-spanned="3">
						<fo:block text-align="center" font-weight="bold"
							border-style="none">Hazardous control action if provided</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">any time</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">too early</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">too late</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-footer>
				<fo:table-row>
					<fo:table-cell>
						<xsl:attribute name="number-columns-spanned"><xsl:value-of
							select="$columns" /></xsl:attribute>
						<fo:block>
							Context Table in context provided</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-footer>
			<fo:table-body>
				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block></fo:block>
					</fo:table-cell>
					<xsl:for-each select="dependenciesForProvided/variableName">
						<fo:table-cell padding="3px">
							<fo:block></fo:block>
						</fo:table-cell>
					</xsl:for-each>

					<fo:table-cell padding="3px">
						<fo:block></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="PMCombisWhenProvided/combinationOfPMValues">
						<xsl:for-each
							select="PMCombisWhenProvided/combinationOfPMValues[valueNames/name]">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="$caTitle" />
									</fo:block>
								</fo:table-cell>
								<xsl:for-each select="valueNames/name">
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="." />
										</fo:block>
									</fo:table-cell>
								</xsl:for-each>

								<xsl:choose>
									<xsl:when test="hazardousAnyTime ='true'">
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
									<xsl:when test="hazardousifProvidedToEarly ='true'">
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
									<xsl:when test="hazardousToLate ='true'">
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
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="$caTitle" />
								</fo:block>
							</fo:table-cell>
							<xsl:for-each select="dependenciesForProvided/variableName">
								<fo:table-cell padding="3px">
									<fo:block>-</fo:block>
								</fo:table-cell>
							</xsl:for-each>

							<fo:table-cell padding="3px"
								number-columns-spanned="3">
								<fo:block>-</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>


	<xsl:template name="contextNOTProvidedTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="caTitle" select="title" />
		<xsl:param name="omitHeader" select="false" />
		<fo:table border="none" space-after="30pt">
			<xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column border-style="solid" />
			<xsl:for-each select="dependenciesForNotProvided/variableName">
				<fo:table-column border-style="solid" />
			</xsl:for-each>
			<fo:table-column column-width="15%" border-style="solid" />

			<xsl:variable name="columns"
				select="count(dependenciesForNotProvided/variableName)+2" />
			<fo:table-header border="solid" background-color="#1A277A"
				color="#FFFFFF" padding="3px">
				<xsl:attribute name="font-size"><xsl:value-of select="$headSize" />pt</xsl:attribute>
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme" />
				<xsl:call-template name="fontTheme" />

				<fo:table-row keep-together.within-column="always">
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Control Action</fo:block>
					</fo:table-cell>
					<xsl:for-each select="dependenciesForNotProvided/variableName">
						<fo:table-cell padding="3px">
							<fo:block wrap-option="wrap" font-weight="bold">
								<xsl:value-of select="." />
							</fo:block>
						</fo:table-cell>
					</xsl:for-each>

					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Hazardous if not provided in this
							context</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-footer>
				<fo:table-row>
					<fo:table-cell>
						<xsl:attribute name="number-columns-spanned"><xsl:value-of
							select="$columns" /></xsl:attribute>
						<fo:block>
							Context Table in context not provided</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-footer>
			<fo:table-body>
				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<fo:table-row>
					<fo:table-cell padding="3px">
						<fo:block></fo:block>
					</fo:table-cell>
					<xsl:for-each select="dependenciesForProvided/variableName">
						<fo:table-cell padding="3px">
							<fo:block></fo:block>
						</fo:table-cell>
					</xsl:for-each>

					<fo:table-cell padding="3px">
						<fo:block></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="PMCombisWhenNotProvided/combinationOfPMValues">
						<xsl:for-each
							select="PMCombisWhenNotProvided/combinationOfPMValues[valueNames/name]">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">#D9D9D9</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:value-of select="$caTitle" />
									</fo:block>
								</fo:table-cell>
								<xsl:for-each select="valueNames/name">
									<fo:table-cell padding="3px">
										<fo:block>
											<xsl:value-of select="." />
										</fo:block>
									</fo:table-cell>
								</xsl:for-each>

								<xsl:choose>
									<xsl:when test="hazardous ='true'">
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
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell padding="3px">
								<fo:block>
									<xsl:value-of select="$caTitle" />
								</fo:block>
							</fo:table-cell>
							<xsl:for-each select="dependenciesForProvided/variableName">
								<fo:table-cell padding="3px">
									<fo:block>-</fo:block>
								</fo:table-cell>
							</xsl:for-each>

							<fo:table-cell padding="3px">
								<fo:block>-</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>