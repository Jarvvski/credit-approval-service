import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { CreditDecisionService } from 'Frontend/generated/endpoints.js';
import { useState } from 'react';
import CreditApplicationModel from "Frontend/generated/com/jarvvski/service/credit/domain/CreditApplicationModel";
import CreditApplication from "Frontend/generated/com/jarvvski/service/credit/domain/CreditApplication";

export default function HelloWorldView() {
  const [name, setName] = useState('');
  const [personalId, setId] = useState('');

  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          label="Your name"
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
        <TextField
          label="Your ID"
          onValueChanged={(e) => {
            setId(e.detail.value);
          }}
        />
        <Button
          onClick={async () => {
              let application = new CreditApplication()
            const serverResponse = await CreditDecisionService.apply(name);
            Notification.show(serverResponse);
          }}
        >
          Say hello
        </Button>
      </section>
    </>
  );
}
