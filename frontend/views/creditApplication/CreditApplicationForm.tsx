import {Button} from '@hilla/react-components/Button.js';
import {TextField} from '@hilla/react-components/TextField.js';
import {useForm} from "@hilla/react-form";
import CreditApplicationDtoModel
    from "Frontend/generated/com/jarvvski/service/credit/application/CreditDecisionService/CreditApplicationDtoModel";
import CreditApplicationDto
    from "Frontend/generated/com/jarvvski/service/credit/application/CreditDecisionService/CreditApplicationDto";
import {useEffect} from "react";
import {IntegerField} from "@hilla/react-components/IntegerField";
import {FormLayout} from "@hilla/react-components/FormLayout";

interface CreditApplicationFormProps {
    application?: CreditApplicationDto | null;
    onSubmit?: (application: CreditApplicationDto) => Promise<void>;
}


export default function CreditApplicationForm({application, onSubmit}: CreditApplicationFormProps) {
    const {field, model, submit, read} =
        useForm(CreditApplicationDtoModel, { onSubmit } );

    useEffect(() => {
        read(application);
    }, [application]);

    const responsiveSteps = [
        { minWidth: '0', columns: 1 },
    ];


    return (
        <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
            <FormLayout responsiveSteps={responsiveSteps}>

                <TextField
                    required
                    label="Name"
                    placeholder={"John Smith"}
                    helperText={"We'd love to get to know you better!"}
                    {...field(model.username)} />
                <TextField
                    required
                    placeholder={"49002010965"}
                    helperText={"This is your credit ID number"}
                    label="Personal ID"
                    {...field(model.personalId)} />
                <IntegerField
                    required
                    label="Amount"
                    defaultValue="2000"
                    helperText={"We only support loans between €2,000 and €10,000"}
                    min={2000}
                    max={10000}
                    clearButtonVisible
                    {...field(model.loanAmount)}>
                    <div slot="prefix">€</div>
                </IntegerField>
                <IntegerField
                    required
                    helperText={"Loan terms can be a minimum of 12 months, and a maximum of 60 months"}
                    label="Loan Term in Months"
                    defaultValue="12"
                    min={12}
                    max={60}
                    {...field(model.loanTermInMonths)} />

                <div className="flex gap-m">
                    <Button onClick={submit} theme="primary">Submit</Button>
                </div>
            </FormLayout>
        </div>
    );
}
