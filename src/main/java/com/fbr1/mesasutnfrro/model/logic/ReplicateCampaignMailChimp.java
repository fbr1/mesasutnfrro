package com.fbr1.mesasutnfrro.model.logic;

import com.ecwid.maleorang.MailchimpMethod;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.annotation.*;

public class ReplicateCampaignMailChimp {
    private final String apiKey, campaignId;


    @Method(httpMethod = HttpMethod.POST, version = APIVersion.v3_0, path = "/campaigns/{campaign_id}/actions/replicate")
    public static class ReplicateCampaignRequest extends MailchimpMethod<ReplicateCampaignResponse> {
        /**
         * This param will be included into the endpoint path.
         */
        @PathParam
        public final String campaign_id;

        /**
         * This param will be included into the request body.
         */
        @Field
        public String id;

        public ReplicateCampaignRequest(String campaignId) {
            this.campaign_id = campaignId;
        }
    }

    public static class ReplicateCampaignResponse extends MailchimpObject {
        @Field
        public String id;

        public String getId() {
            return id;
        }
    }


    public ReplicateCampaignMailChimp(String apiKey, String campaignId) {
        this.apiKey = apiKey;
        this.campaignId = campaignId;
    }

}

