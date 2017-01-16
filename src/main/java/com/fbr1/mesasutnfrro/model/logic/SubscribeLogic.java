package com.fbr1.mesasutnfrro.model.logic;

import com.ecwid.maleorang.MailchimpClient;
import com.ecwid.maleorang.MailchimpException;
import com.ecwid.maleorang.method.v3_0.members.EditMemberMethod;
import com.ecwid.maleorang.method.v3_0.members.MemberInfo;
import com.fbr1.mesasutnfrro.model.forms.SubscribeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscribeLogic {

    @Value("${MAILCHIMP_API_KEY}")
    private String API_KEY;

    @Value("${MAILCHIMP_LIST_ID}")
    private String LIST_ID;

    @Value("${MAILCHIMP_CAMPAIGN_ID}")
    private String CAMPAIGN_ID;

    private final static Logger logger = LoggerFactory.getLogger(SubscribeLogic.class);

    public void saveSubscriber(SubscribeForm subscribeForm) throws IOException, MailchimpException{

        try(MailchimpClient client = new MailchimpClient(API_KEY)){
            EditMemberMethod.CreateOrUpdate method = new EditMemberMethod
                    .CreateOrUpdate(LIST_ID, subscribeForm.getEmail());
            method.status = "pending";

            MemberInfo member = client.execute(method);
            logger.info("The user has been successfully subscribed: " + member);
        }

    }

    public void sendCampaign() throws IOException, MailchimpException{

        try(MailchimpClient client = new MailchimpClient(API_KEY)){

            // Replicate existing campaign
            ReplicateCampaignMailChimp.ReplicateCampaignRequest replicateRequest = new ReplicateCampaignMailChimp
                    .ReplicateCampaignRequest(CAMPAIGN_ID);

            ReplicateCampaignMailChimp.ReplicateCampaignResponse replicateResponse = client.execute(replicateRequest);


            // Send Campaign
            SendCampaignMailChimp.SendCampaignRequest request = new SendCampaignMailChimp
                    .SendCampaignRequest(replicateResponse.getId());

            client.execute(request);
        }

    }

}
