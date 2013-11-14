package dk.kb.yggdrasil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.antiaction.common.json.JSONDecoder;
import com.antiaction.common.json.JSONEncoder;
import com.antiaction.common.json.JSONEncoding;
import com.antiaction.common.json.JSONException;
import com.antiaction.common.json.JSONObjectMappings;
import com.antiaction.common.json.JSONStructure;
import com.antiaction.common.json.JSONText;
import com.antiaction.common.json.annotation.JSONNullable;

import dk.kb.yggdrasil.exceptions.YggdrasilException;

/**
 * Small class for marshalling JSON messages to/from Valhal.
 * Also includes java class representations of the data.
 */
public class JSONMessaging {

    /**
     * JSON preservation request object representation.
     */
    public static class PreservationRequest {

        /** Valhal element UUID. */
        public String UUID;

        /** Preservation profile. */
        public String Preservation_profile;

        /** Preservation state update URI. */
        public String Update_URI;

        /** Optional content UUID. */
        @JSONNullable
        public String File_UUID;

        /** Optional content URI. */
        @JSONNullable
        public String Content_URI;

        /** Metadata data. */
        public Metadata metadata;

    }

    /**
     * JSON metadata object representation.
     */
    public static class Metadata {

        /** Descriptive metadata. */
        @JSONNullable
        public String descMetadata;

        /** Provenance metadata. */
        @JSONNullable
        public String provenanceMetadata;

        /** Preservation metadata. */
        @JSONNullable
        public String preservationMetadata;

        /** technical metadata. */
        @JSONNullable
        public String techMetadata;

    }

    /**
     * JSON preservation response object representation.
     * The single containing object reflects the JSON structure.
     */
    public static class PreservationResponse {

        /** Preservation data. */
        public Preservation preservation;

    }

    /**
     * JSON preservation object representation.
     */
    public static class Preservation {

        /** Preservation state. */
        public String preservation_state;

        /** Preservation details. */
        public String preservation_details;

        /** WARC ID, optional. */
        @JSONNullable
        public String warc_id;

    }

    /** Logging mechanism. */
    private static Logger logger = LoggerFactory.getLogger(JSONMessaging.class.getName());

    /** JSON encoding encoder/decoder dispatcher. */
    private static JSONEncoding json_encoding = JSONEncoding.getJSONEncoding();

    /** JSON object mapping worker. */
    private  static JSONObjectMappings json_om = new JSONObjectMappings();

    /** JSON decoder/encoder. */
    private static JSONText json_text;

    /**
     * Initialize JSON marshaller.
     */
    static {
        json_text = new JSONText();
        try {
            json_om.register(PreservationRequest.class);
            json_om.register(PreservationResponse.class);
        } catch (JSONException e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * Convert JSON data into a preservation request object.
     * @param in <code>InputStream</code> containing JSON data
     * @return preservation request object representation
     * @throws YggdrasilException if an I/O error occurs while unmashalling
     */
    public static PreservationRequest getPreservationRequest(PushbackInputStream in) throws YggdrasilException {
        try {
            int encoding = JSONEncoding.encoding(in);
            JSONDecoder json_decoder = json_encoding.getJSONDecoder(encoding);
            JSONStructure json_object = json_text.decodeJSONtext(in, json_decoder);
            PreservationRequest request = json_om.getStructureUnmarshaller().toObject(json_object, PreservationRequest.class);
            return request;
        } catch (IOException e) {
            throw new YggdrasilException("IOException unmarshalling preservation request.", e);
        } catch (JSONException e) {
            throw new YggdrasilException("JSONException unmarshalling preservation request.", e);
        }
    }

    /**
     * Convert preservation response object into JSON data.
     * @param response preservation response object
     * @return preservation response as JSON data
     * @throws YggdrasilException if an I/O error occurs while marshalling
     */
    public static byte[] getPreservationResponse(PreservationResponse response) throws YggdrasilException {
        try {
            JSONEncoder json_encoder = json_encoding.getJSONEncoder(JSONEncoding.E_UTF8);
            JSONStructure json_object = json_om.getStructureMarshaller().toJSON(response);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            json_text.encodeJSONtext(json_object, json_encoder, false, bout);
            bout.close();
            byte[] content = bout.toByteArray();
            return content;
        } catch (IOException e) {
            throw new YggdrasilException("IOException marshalling preservation response.", e);
        } catch (JSONException e) {
            throw new YggdrasilException("JSONException marshalling preservation response.", e);
        }
    }

}