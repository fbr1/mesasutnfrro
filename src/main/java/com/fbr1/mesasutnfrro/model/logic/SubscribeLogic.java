package com.fbr1.mesasutnfrro.model.logic;

import com.ecwid.maleorang.MailchimpClient;
import com.ecwid.maleorang.MailchimpException;
import com.ecwid.maleorang.method.v3_0.members.EditMemberMethod;
import com.ecwid.maleorang.method.v3_0.members.MemberInfo;
import com.fbr1.mesasutnfrro.model.forms.SubscribeForm;
import com.fbr1.mesasutnfrro.model.logic.mailchimp.CreateCampaignMailChimp;
import com.fbr1.mesasutnfrro.model.logic.mailchimp.EditContentCampaignMailChimp;
import com.fbr1.mesasutnfrro.model.logic.mailchimp.SendCampaignMailChimp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SubscribeLogic {

    @Value("${MAILCHIMP_API_KEY}")
    private String API_KEY;

    @Value("${MAILCHIMP_LIST_ID}")
    private String LIST_ID;

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

    /**
     * Sends the campaign to its subscribers list
     *
     *@param campaign_id - String: Id of the campaign to send
     */
    public void sendCampaign(String campaign_id) throws IOException, MailchimpException{

        try(MailchimpClient client = new MailchimpClient(API_KEY)){

            // Send Campaign
            SendCampaignMailChimp.SendCampaignRequest request = new SendCampaignMailChimp
                    .SendCampaignRequest(campaign_id);

            client.execute(request);

        }

    }

    /**
     * Creates a new campaign containing the current date
     *
     * @return      String: Campaign ID
     */
    public String createNewCampaign() throws IOException, MailchimpException{

        try(MailchimpClient client = new MailchimpClient(API_KEY)){

            //
            // Create campaign
            //

            CreateCampaignMailChimp.CreateCampaignRequest createRequest = new CreateCampaignMailChimp
                    .CreateCampaignRequest();

            // Setup Settings object

            CreateCampaignMailChimp.Settings settings = new CreateCampaignMailChimp.Settings();

            // Find current date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();

            settings.title = "Nuevas Mesas Cargadas " + dtf.format(localDate);
            settings.from_name = "UTN FRRo Mesas";
            settings.reply_to = "utnfrromesas@yahoo.com";
            settings.subject_line = "Nuevas Mesas Cargadas";

            createRequest.type = "regular";
            createRequest.settings = settings;
            createRequest.recipients = new CreateCampaignMailChimp.Recipients(LIST_ID);

            CreateCampaignMailChimp.CreateCampaignResponse createResponse = client.execute(createRequest);

            String campaign_id = createResponse.getId();

            //
            // Assign the template to the campaign
            //

            EditContentCampaignMailChimp.EditContentCampaignRequest editContentRequest = new EditContentCampaignMailChimp.
                    EditContentCampaignRequest(campaign_id);

            editContentRequest.template = new EditContentCampaignMailChimp.Template(66129);

            client.execute(editContentRequest);

            return campaign_id;
        }

    }

    public SubscribeLogic() { }

    public SubscribeLogic(String API_KEY, String LIST_ID) {
        this.API_KEY = API_KEY;
        this.LIST_ID = LIST_ID;
    }
}
