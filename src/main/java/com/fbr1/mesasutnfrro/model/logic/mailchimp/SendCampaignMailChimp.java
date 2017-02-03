package com.fbr1.mesasutnfrro.model.logic.mailchimp;

import com.ecwid.maleorang.MailchimpMethod;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.annotation.*;

public class SendCampaignMailChimp {
    private final String apiKey, campaignId;


    @Method(httpMethod = HttpMethod.POST, version = APIVersion.v3_0, path = "/campaigns/{campaign_id}/actions/send")
    public static class SendCampaignRequest extends MailchimpMethod<SendCampaignResponse> {
        /**
         * This param will be included into the endpoint path.
         */
        @PathParam
        public final String campaign_id;

        public SendCampaignRequest(String campaignId) {
            this.campaign_id = campaignId;
        }
    }

    public static class SendCampaignResponse extends MailchimpObject {

    }


    public SendCampaignMailChimp(String apiKey, String campaignId) {
        this.apiKey = apiKey;
        this.campaignId = campaignId;
    }

}

