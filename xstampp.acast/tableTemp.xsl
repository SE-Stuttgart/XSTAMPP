<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- ############################################################ -->
	<!-- ################### Templates of the PDF ################### -->
	<!-- ############################################################ -->

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



	<!-- ################### Hazard-Table ################### -->
	<xsl:template name="hazardTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="omitHeader" select="false" />
		<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>

		<fo:table border="none" space-after="30pt">
			<xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="65%"
				border-style="none" />

			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme" />
				<xsl:call-template name="fontTheme" />
				<fo:table-row>
					<xsl:attribute name="font-size"><xsl:value-of
						select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Title</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some hazards are defined -->
					<xsl:when test="haz/hazards/hazard">
						<xsl:for-each select="haz/hazards/hazard">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
					#D9D9D9
					</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="number" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="title" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="title" select="description" />
										</xsl:call-template>
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
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- ################### Safety Constraints Table ################### -->
	<xsl:template name="safetyConstraintsTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="omitHeader" select="false" />
		<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>

		<fo:table border="none" space-after="30pt">
			<xsl:attribute name="table-omit-header-at-break"><xsl:value-of select="$omitHeader" /></xsl:attribute>
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="30%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="65%"
				border-style="none" />

			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme" />
				<xsl:call-template name="fontTheme" />
				<fo:table-row>
					<xsl:attribute name="font-size"><xsl:value-of
						select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">No.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Safety Constraint</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
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
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="number" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="title" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="description" />
										</xsl:call-template>
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
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- ################### Proximal Events Table ################### -->
	<xsl:template name="proxEventsTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="omitHeader" select="false" />
		<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>

		<fo:table border="none" space-after="30pt">
			<fo:table-column column-number="1" column-width="5%"
				border-style="none" />
			<fo:table-column column-number="2" column-width="15%"
				border-style="none" />
			<fo:table-column column-number="3" column-width="20%"
				border-style="none" />
			<fo:table-column column-number="4" column-width="60%"
				border-style="none" />
			<fo:table-header border="none" background-color="#1A277A"
				color="#FFFFFF">
				<!-- Sets the PDF-Theme-Color -->
				<xsl:call-template name="headTheme" />
				<xsl:call-template name="fontTheme" />
				<fo:table-row>
					<xsl:attribute name="font-size"><xsl:value-of
						select="$headSize" />pt</xsl:attribute>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Date</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Time</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3px">
						<fo:block font-weight="bold">Description</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
				<xsl:choose>
					<!-- Checks whether some Proximal Events are defined -->
					<xsl:when test="proxEvents/eventList/event">
						<xsl:for-each select="proxEvents/eventList/event">
							<fo:table-row border="none">
								<xsl:if test="position() mod 2 = 0">
									<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
								</xsl:if>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="ID" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="date" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="time" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block>
										<xsl:call-template name="intersperse-with-zero-spaces">
											<xsl:with-param name="str" select="description" />
										</xsl:call-template>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<!-- If there are no Proximal Events defined the first row -->
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

	<!-- ################### Responsibilities Table ################### -->
	<xsl:template name="respTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="omitHeader" select="false" />
		<xsl:param name="title.size" select="24" />

		<xsl:variable name="bcolor" select="exportinformation/backgroundColor" />
		<xsl:variable name="fcolor" select="exportinformation/fontColor" />

		<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
		<!-- Checks whether some Responsibilities are defined -->
		<xsl:choose>
			<xsl:when test="crc">
				<fo:block space-after="5pt" page-break-after="avoid">
					<xsl:attribute name="font-size"><xsl:value-of
						select="$title.size" />pt</xsl:attribute>
					Roles and Responsibilities
				</fo:block>
				<xsl:for-each select="crc/componentNames/entry">
					<xsl:variable name="currentComponent" select="." />

					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Safety Related Responsibilities:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//responsibility[name=$currentComponent/key]">
									<xsl:for-each select="//responsibility[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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

					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Unsafe Decisions and Control Actions:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//unsafeAction[name=$currentComponent/key]">
									<xsl:for-each select="//unsafeAction[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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


					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Process/Mental Model Flaws:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//flaw[name=$currentComponent/key]">
									<xsl:for-each select="//flaw[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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

					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Context in which decisions made:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//context[name=$currentComponent/key]">
									<xsl:for-each select="//context[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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
					
					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Feedback:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//feedback[name=$currentComponent/key]">
									<xsl:for-each select="//feedback[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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
					
					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Coordination:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//coordination[name=$currentComponent/key]">
									<xsl:for-each select="//coordination[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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


				</xsl:for-each>


			</xsl:when>
			<xsl:otherwise>

			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ################### Recommendation Table ################### -->
	<xsl:template name="recommendationTable">
		<xsl:param name="varSize" select="12" />
		<xsl:param name="headSize" select="14" />
		<xsl:param name="omitHeader" select="false" />
		<xsl:param name="title.size" select="24" />
		<xsl:variable name="bcolor" select="exportinformation/backgroundColor" />
		<xsl:variable name="fcolor" select="exportinformation/fontColor" />


		<xsl:attribute name="font-size"><xsl:value-of select="$varSize" />pt</xsl:attribute>
		<xsl:choose>
			<xsl:when test="//recommendation">
				<fo:block space-after="5pt" page-break-after="avoid">
					<xsl:attribute name="font-size"><xsl:value-of
						select="$title.size" />pt</xsl:attribute>
					Findings and Recommendations
				</fo:block>
				<xsl:for-each select="crc/componentNames/entry">
					<xsl:variable name="currentComponent" select="." />

					<fo:block space-after="5pt" page-break-after="avoid">
						<xsl:attribute name="font-size">24pt</xsl:attribute>
						Findings and Recommendations:
						<xsl:value-of select="key" />
					</fo:block>
					<fo:table border="none" space-after="30pt">
						<fo:table-column column-number="1" column-width="20%"
							border-style="none" />
						<fo:table-column column-number="2" column-width="80%"
							border-style="none" />
						<fo:table-header border="none" background-color="#1A277A"
							color="#FFFFFF">
							<!-- Sets the PDF-Theme-Color -->
							<xsl:call-template name="headTheme" />
							<xsl:call-template name="fontTheme" />
							<fo:table-row>
								<!-- Sets the PDF-Theme-Color -->
								<xsl:attribute name="background-color"> 
										 <xsl:value-of select="$bcolor" />
										</xsl:attribute>
								<xsl:attribute name="color"> 
										 <xsl:value-of select="$fcolor" />
										</xsl:attribute>
								<xsl:attribute name="font-size"><xsl:value-of
									select="$headSize" />pt</xsl:attribute>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">ID</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="3px">
									<fo:block font-weight="bold">Description</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:attribute name="font-size"><xsl:value-of
								select="$varSize" />pt</xsl:attribute>
							<xsl:choose>
								<!-- Checks whether some Resp are defined -->
								<xsl:when test="//recommendation[name=$currentComponent/key]">
									<xsl:for-each select="//recommendation[name=$currentComponent/key]">
										<fo:table-row border="none">
											<xsl:if test="position() mod 2 = 0">
												<xsl:attribute name="background-color">
									#D9D9D9
									</xsl:attribute>
											</xsl:if>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="id" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding="3px">
												<fo:block>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str" select="description" />
													</xsl:call-template>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:when>
								<!-- If there are no Resp defined the first row -->
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


				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>

			</xsl:otherwise>
		</xsl:choose>
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