package com.fbr1.mesasutnfrro.model.logic.mailchimp;

import com.ecwid.maleorang.MailchimpMethod;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.annotation.*;

public class CreateCampaignMailChimp {
    private final String apiKey;


    @Method(httpMethod = HttpMethod.POST, version = APIVersion.v3_0, path = "/campaigns")
    public static class CreateCampaignRequest extends MailchimpMethod<CreateCampaignResponse> {

        /**
         * This param will be included into the request body.
         */
        @Field
        public String id;

        @Field
        public Recipients recipients;

        @Field
        public String type;

        @Field
        public Settings settings;

        public CreateCampaignRequest() {

        }
    }

    public static class Recipients extends MailchimpObject {
        @Field
        public String list_id;

        public Recipients(String list_id){
            this.list_id = list_id;
        }
    }

    public static class Settings extends MailchimpObject {
        @Field
        public String title;

        @Field
        public String subject_line;

        @Field
        public String reply_to;

        @Field
        public String from_name;
    }

    public static class CreateCampaignResponse extends MailchimpObject {
        @Field
        public String id;

        public String getId() {
            return id;
        }
    }

    public CreateCampaignMailChimp(String apiKey) {
        this.apiKey = apiKey;
    }

}

