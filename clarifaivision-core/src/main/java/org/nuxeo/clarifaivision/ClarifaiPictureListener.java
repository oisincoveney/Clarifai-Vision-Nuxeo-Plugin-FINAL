/*
 * (C) Copyright 2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *      Oisin Coveney
 */
package org.nuxeo.clarifaivision;


import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;


/**
 * Handles the listener event, creating a Clarifai Tagging operation chain
 * that tags a Picture object with tags from the Clarifai image prediction
 * service (clarifai.com)
 *
 * Generated by NuxeoCLI
 */
public class ClarifaiPictureListener implements EventListener
{
    @Override
    public void handleEvent ( Event event )
    {
        EventContext ctx = event.getContext();
        if ( !( ctx instanceof DocumentEventContext ) )
        {
            return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel        doc    = docCtx.getSourceDocument();


        // Get the core session and set up Automation Service for the operation
        CoreSession       session    = doc.getCoreSession();
        AutomationService automation = Framework.getService(AutomationService.class);

        // Add input and CoreSession for the Operation Chain
        OperationContext opContext = new OperationContext();
        opContext.setInput(doc);
        opContext.setCoreSession(session);

        // Name the operation chain and add the Clarifai Tagger Operation
        OperationChain chain = new OperationChain("clarifai-operation-chain");
        chain.add("Document.ClarifaiTaggerOperation");

        // Run the operation chain through the Automation Service
        try
        {
            automation.run(opContext, chain);
        }
        catch ( OperationException e )
        {
            e.printStackTrace();
        }
    }
}
