package com.developer.ck.checkin.util;

import com.developer.ck.checkin.R;

import java.io.IOException;

import sqip.CardDetails;
import sqip.CardEntryActivityCommand;
import sqip.CardNonceBackgroundHandler;

public class CardEntryBackgroundHandler {

}
////
//public class CardEntryBackgroundHandler implements CardNonceBackgroundHandler {
//    @Override
//    public CardEntryActivityCommand handleEnteredCardInBackground(CardDetails cardDetails) {
//        try {
//            // TODO Call your backend service
////            MyBackendServiceResponse response = // myBackendService(cardDetails.getNonce());...
////
////            if (response.isSuccessful()) {
////                return new CardEntryActivityCommand.Finish();
////            } else {
////                return new CardEntryActivityCommand.ShowError(response.errorMessage)
////            }
//        } catch(IOException exception) {
//            return new CardEntryActivityCommand.ShowError(
//                    resources.getString(R.string.network_failure));
//        }
//    }
//}

