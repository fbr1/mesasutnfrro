package com.fbr1.mesasutnfrro.model.logic.mailchimp;

import com.ecwid.maleorang.MailchimpMethod;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.annotation.*;

public class EditContentCampaignMailChimp {
    private final String apiKey, campaignId;


    @Method(httpMethod = HttpMethod.PUT, version = APIVersion.v3_0, path = "/campaigns/{campaign_id}/content")
    public static class EditContentCampaignRequest extends MailchimpMethod<EditContentCampaignResponse> {
        /**
         * This param will be included into the endpoint path.
         */
        @PathParam
        public final String campaign_id;

        /**
         * This param will be included into the request body.
         */
        @Field
        public Template template;

        public EditContentCampaignRequest(String campaignId) {
            this.campaign_id = campaignId;
        }
    }

    public static class Template extends MailchimpObject {
        @Field
        public int id;

        public Template(int template_id){
            this.id = template_id;
        }
    }

    public static class EditContentCampaignResponse extends MailchimpObject {
    }


    public EditContentCampaignMailChimp(String apiKey, String campaignId) {
        this.apiKey = apiKey;
        this.campaignId = campaignId;
    }

}

