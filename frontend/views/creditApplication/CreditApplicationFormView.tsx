import CreditApplicationDto
    from "Frontend/generated/com/jarvvski/service/credit/application/CreditDecisionService/CreditApplicationDto";
import {CreditDecisionService} from "Frontend/generated/endpoints";
import Decision from "Frontend/generated/com/jarvvski/service/credit/domain/Decision";
import {Notification} from "@hilla/react-components/Notification.js";
import {useState} from "react";
import CreditDecision from "Frontend/generated/com/jarvvski/service/credit/domain/CreditDecision";
import CreditApplicationForm from "Frontend/views/creditApplication/CreditApplicationForm";

export default function CreditApplicationFormView() {

    const [decision, setDecision] = useState<CreditDecision>();
    const [application] = useState<CreditApplicationDto>();

    async function onApplicationSubmitted(application: CreditApplicationDto) {
        const response = await CreditDecisionService.apply(application)
        switch (response?.decision) {
            case Decision.APPROVED: Notification.show(
                "Approved for a maximum of: €" + response?.loan?.amount?.data,
                {theme: "success"});
                break;
            case Decision.DECLINED_UNKNOWN_CREDIT: Notification.show(
                "We couldn't seem to calculate a credit score for you...",
                {theme: "warning"});
                break;
            case Decision.DECLINED_BAD_CREDIT: Notification.show(
                "Unfortunately, we cannot approve you at this time",
                {theme: "error"}
            )
        }
        setDecision(response);
    }

    return (
        <div className="p-m flex gap-m">
            {
                <CreditApplicationForm application={application} onSubmit={onApplicationSubmitted}/>
            }
        </div>
    );
}