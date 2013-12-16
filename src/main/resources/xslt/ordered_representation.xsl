<?xml version="1.0" encoding="UTF-8"?> 
<xsl:transform version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:java="http://xml.apache.org/xalan/java"
    xmlns:mets="http://www.loc.gov/METS/"
    xmlns:mix="http://www.loc.gov/mix/v20"
    xmlns:mods="http://www.loc.gov/mods/v3"
    xmlns:premis="info:lc/xmlns/premis-v2"

    extension-element-prefixes="java">

  <xsl:output encoding="UTF-8" method="xml" indent="yes" />

  <xsl:template match="metadata">
    <xsl:call-template name="orderedrepresentation_mets_generator" />
  </xsl:template>

  <xsl:template name="orderedrepresentation_mets_generator">
    <mets:mets xsi:schemaLocation="http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/version191/mets.xsd">
      <xsl:attribute name="TYPE">
        <xsl:value-of select="'OrderedRepresentation'" />
      </xsl:attribute>
      <xsl:attribute name="OBJID">
        <xsl:value-of select="provenanceMetadata/fields/uuid" />
      </xsl:attribute>
      <xsl:attribute name="PROFILE">
        <xsl:value-of select="java:dk.kb.metadata.Constants.getProfileURL()" />
      </xsl:attribute>

      <!-- START metsHdr -->
      <xsl:element name="mets:metsHdr">
        <xsl:attribute name="CREATEDATE">
          <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
        </xsl:attribute>

        <xsl:element name="mets:agent">
          <xsl:attribute name="ID">
            <xsl:value-of select="'kbDk'" />
          </xsl:attribute>
          <xsl:attribute name="ROLE"> 
            <xsl:value-of select="'CREATOR'" />
          </xsl:attribute>
          <xsl:attribute name="TYPE">
            <xsl:value-of select="'ORGANIZATION'" />
          </xsl:attribute>
          <xsl:element name="mets:name">
            <xsl:value-of select="'Det Kongelige Bibliotek, Nationalbibliotek og Københavns Universitetsbibliotek'" />
          </xsl:element>
        </xsl:element>

        <xsl:element name="mets:agent">
          <xsl:attribute name="ID">
            <xsl:value-of select="'kbDkYggdrasil1.0'" />
          </xsl:attribute>
          <xsl:attribute name="ROLE"> 
            <xsl:value-of select="'CREATOR'" />
          </xsl:attribute>
          <xsl:attribute name="TYPE">
            <xsl:value-of select="'OTHER'" />
          </xsl:attribute>
          <xsl:attribute name="OTHERTYPE">
            <xsl:value-of select="'API'" />
          </xsl:attribute>
          <xsl:element name="mets:name">
            <xsl:value-of select="'KB Yggdrasil v. 1.0'" />
          </xsl:element>
          <xsl:element name="mets:note">
            <xsl:value-of select="'dk.kb.yggdrasil.MetsGenerator (??)'" />
          </xsl:element>
        </xsl:element>

        <xsl:for-each select="field[@name='Department']">
          <xsl:element name="mets:agent">
            <xsl:attribute name="ID">
              <xsl:value-of select="'kbDkNsa'" />
            </xsl:attribute>
            <xsl:attribute name="ROLE">
              <xsl:value-of select="'EDITOR'" />
            </xsl:attribute>
            <xsl:attribute name="TYPE">
              <xsl:value-of select="'OTHER'" />
            </xsl:attribute>
            <xsl:attribute name="OTHERTYPE">
              <xsl:value-of select="'DEPARTMENT'" />
            </xsl:attribute>
            <xsl:element name="mets:name">
              <xsl:value-of select="'NSA: Nationalssamlingsafdelingen'" />
            </xsl:element>
          </xsl:element>
        </xsl:for-each>
      </xsl:element>
      <!-- END metsHdr -->

      <!-- START dmdSec -->
      <xsl:element name="mets:dmdSec">
        <xsl:attribute name="CREATED">
          <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
        </xsl:attribute>    
        <xsl:attribute name="ID">
          <xsl:value-of select="'Mods1'" />
        </xsl:attribute>
        <xsl:element name="mets:mdWrap">
          <xsl:attribute name="MDTYPE">
            <xsl:value-of select="'MODS'" />
          </xsl:attribute>
          <xsl:element name="mets:xmlData">

            <mods:mods version="3.4" xmlns:mods="http://www.loc.gov/mods/v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-4.xsd">
              <mods:titleInfo>
                <xsl:element name="mods:title">
                  <xsl:value-of select="provenanceMetadata/fields/label" />
                </xsl:element>
              </mods:titleInfo>
            </mods:mods>

          </xsl:element>
        </xsl:element>
      </xsl:element>
      <!-- END dmdSec -->

      <!-- START amdSec -->
      <xsl:element name="mets:amdSec">
        <!-- ADD MODS (rights) -->
        <xsl:element name="mets:rightsMD">
          <xsl:attribute name="CREATED">
            <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
          </xsl:attribute>
          <xsl:attribute name="ID">
            <xsl:value-of select="'ModsRights1'" />
          </xsl:attribute>
          <xsl:element name="mets:mdWrap">
            <xsl:attribute name="MDTYPE">
              <xsl:value-of select="'MODS'" />
            </xsl:attribute>
            <xsl:element name="mets:xmlData">
              <mods:mods xsi:schemaLocation="http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-4.xsd" version="3.4">
                <xsl:element name="mods:accessCondition">
                  <xsl:value-of select="'Det Kongelige Bibliotek'" />
                </xsl:element>
              </mods:mods>
            </xsl:element>
          </xsl:element>
        </xsl:element>
        <!-- ADD PREMIS -->
        <xsl:element name="mets:digiprovMD">
          <xsl:attribute name="CREATED">
            <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
          </xsl:attribute>
          <xsl:attribute name="ID">
            <xsl:value-of select="'Premis1'" />
          </xsl:attribute>
          <xsl:element name="mets:mdWrap">
            <xsl:attribute name="MDTYPE">
              <xsl:value-of select="'PREMIS'" />
            </xsl:attribute>
            <xsl:element name="mets:xmlData">
              <!-- Preservation level for bit safety. -->
              <premis:preservationLevel xsi:schemaLocation="info:lc/xmlns/premis-v2 http://www.loc.gov/standards/premis/v2/premis-v2-2.xsd">
                <xsl:element name="premis:preservationLevelValue">
                  <xsl:value-of select="'preservation/bitSafety'" />
                </xsl:element>
                <xsl:element name="premis:preservationLevelDateAssigned">
                  <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
                </xsl:element>
              </premis:preservationLevel>
              <!-- Preservation level for confidentiality. -->
              <premis:preservationLevel xsi:schemaLocation="info:lc/xmlns/premis-v2 http://www.loc.gov/standards/premis/v2/premis-v2-2.xsd">
                <xsl:element name="premis:preservationLevelValue">
                  <xsl:value-of select="'preservation/confidentality'" />
                </xsl:element>
                <xsl:element name="premis:preservationLevelDateAssigned">
                  <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
                </xsl:element>
              </premis:preservationLevel>
            </xsl:element>
          </xsl:element>
        </xsl:element>
        <!-- ADD PREMIS:EVENT -->
        <xsl:element name="mets:digiprovMD">
          <xsl:attribute name="CREATED">
            <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
          </xsl:attribute>
          <xsl:attribute name="ID">
            <xsl:value-of select="'PremisEvent1'" />
          </xsl:attribute>
          <xsl:element name="mets:mdWrap">
            <xsl:attribute name="MDTYPE">
              <xsl:value-of select="'PREMIS:EVENT'" />
            </xsl:attribute>
            <xsl:element name="mets:xmlData">
              <premis:event xsi:schemaLocation="info:lc/xmlns/premis-v2 http://www.loc.gov/standards/premis/v2/premis-v2-2.xsd">
                <xsl:element name="premis:eventIdentifier">
                  <xsl:element name="premis:eventIdentifierType">
                    <xsl:value-of select="'UUID'" />
                  </xsl:element>
                  <xsl:element name="premis:eventIdentifierValue">
                    <xsl:value-of select="'RandomUUID'" />
                  </xsl:element>
                </xsl:element>
                <xsl:element name="premis:eventType">
                  <xsl:value-of select="'ingestion'" />
                </xsl:element>
                <xsl:element name="premis:eventDateTime">
                  <xsl:value-of select="java:dk.kb.metadata.utils.CalendarUtils.getCurrentDate()" />
                </xsl:element>
                <xsl:element name="premis:linkingAgentIdentifier">
                  <xsl:element name="premis:linkingAgentIdentifierType">
                    <xsl:value-of select="'UUID'" />
                  </xsl:element>
                  <xsl:element name="premis:linkingAgentIdentifierValue">
                    <xsl:value-of select="'RandomUUID'" />
                  </xsl:element>
                </xsl:element>
                <xsl:element name="premis:linkingObjectIdentifier">
                  <xsl:element name="premis:linkingObjectIdentifierType">
                    <xsl:value-of select="'UUID'" />
                  </xsl:element>
                  <xsl:element name="premis:linkingObjectIdentifierValue">
                    <xsl:value-of select="'RandomUUID'" />
                  </xsl:element>
                </xsl:element>
              </premis:event>
            </xsl:element>
          </xsl:element>
        </xsl:element>
      </xsl:element>
      <!-- END amdSec -->

      <!-- START structMap -->
      <xsl:element name="mets:structMap">
        <xsl:attribute name="TYPE">
          <xsl:value-of select="'logical'" />
        </xsl:attribute>

        <xsl:element name="mets:div">
          <xsl:attribute name="DMDID">
            <xsl:value-of select="'Mods1'" />
          </xsl:attribute>
          <xsl:attribute name="ADMID">
            <xsl:value-of select="'ModsRights1 Premis1 PremisEvent1'" />
          </xsl:attribute>
          <xsl:for-each select="techMetadata/mets:mets/mets:structMap/mets:div">
            <xsl:element name="mets:div">
              <xsl:attribute name="ID">
                <xsl:value-of select="@ID" />
              </xsl:attribute>
              <xsl:attribute name="ORDER">
                <xsl:value-of select="@ORDER" />
              </xsl:attribute>
<!--
              <xsl:call-template name="orderedrepresentation_mptr">
                <xsl:with-param name="fptr" select="mets:fptr" />
              </xsl:call-template>
-->
              <xsl:element name="mets:mptr">
                <xsl:attribute name="LOCTYPE">
                  <xsl:value-of select="'URN'" />
                </xsl:attribute>
                <xsl:attribute name="xlink:href">
                  <xsl:value-of select="concat('urn:uuid:', mets:fptr/@FILEID)" />
                </xsl:attribute>
              </xsl:element>

            </xsl:element>
          </xsl:for-each>
        </xsl:element>
      </xsl:element>
      <!-- END structMap -->

    </mets:mets>
  </xsl:template>
<!--
  <xsl:template name="orderedrepresentation_mptr">
    <xsl:param name="fptr" />
    <xsl:variable name="FILEID" select="$fptr/@FILEID" />
    <xsl:element name="mets:mptr">
      <xsl:attribute name="LOCTYPE">
        <xsl:value-of select="'URN'" />
      </xsl:attribute>
      <xsl:attribute name="xlink:href">
        <xsl:value-of select="concat('urn:uuid:', $FILEID)" />
      </xsl:attribute>
    </xsl:element>
  </xsl:template>
-->
</xsl:transform>