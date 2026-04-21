package uk.gov.onelogin.sharing.testapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import uk.gov.onelogin.sharing.orchestration.verificationrequest.AttributeGroup
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest.Companion.VerificationRequestType
import uk.gov.onelogin.sharing.sdk.api.presenter.CredentialPresenter
import uk.gov.onelogin.sharing.sdk.api.verifier.CredentialVerifier
import uk.gov.onelogin.sharing.testapp.MockCredential.Companion.MockCredentialType
import uk.gov.onelogin.sharing.ui.impl.ShareCredential
import uk.gov.onelogin.sharing.ui.impl.VerifyCredential

object TestAppNavigationExt {
    fun NavController.navigateToTestAppHolderJourney(
        credential: MockCredential,
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.Holder(credential = credential),
        options
    )

    internal fun NavGraphBuilder.configureHolderJourneyWrapper(
        navController: NavController,
        component: (MockCredential) -> CredentialPresenter
    ) {
        composable<CredentialSharingDestination.Holder>(
            typeMap = mapOf(
                typeOf<MockCredential>() to MockCredentialType
            )
        ) { navBackStackEntry ->
            val arguments: CredentialSharingDestination.Holder = navBackStackEntry.toRoute()
            val presenter = remember { component(arguments.credential) }

            ShareCredential(
                component = presenter,
                modifier = Modifier.fillMaxSize()
            )

            Box {
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = {
                        presenter.orchestrator.cancel()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_close_clear_cancel
                        ),
                        contentDescription = "Close"
                    )
                }
            }
        }
    }

    fun NavController.navigateToTestAppVerifierJourney(
        request: VerificationRequest,
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.Verifier(request = request),
        options
    )

    internal fun NavGraphBuilder.configureVerifierJourneyWrapper(
        navController: NavController,
        requestToVerifier: (VerificationRequest) -> CredentialVerifier
    ) {
        composable<CredentialSharingDestination.Verifier>(
            typeMap = mapOf(
                typeOf<VerificationRequest>() to VerificationRequestType
            )
        ) { navBackStackEntry ->
            val arguments: CredentialSharingDestination.Verifier = navBackStackEntry.toRoute()
            val verifier = remember { requestToVerifier(arguments.request) }

            VerifyCredential(
                component = verifier,
                modifier = Modifier.fillMaxSize()
            )

            Box {
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = {
                        verifier.orchestrator.cancel()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_close_clear_cancel
                        ),
                        contentDescription = "Close"
                    )
                }
            }
        }
    }

    fun NavController.navigateToHolderCredentialSelection(
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.SelectCredential,
        options
    )

    internal fun NavGraphBuilder.configureSelectMockCredential(
        mockCredentials: List<MockCredential>,
        onSelectCredential: (MockCredential) -> Unit = {}
    ) {
        dialog<CredentialSharingDestination.SelectCredential> {
            Surface(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.select_credential),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    LazyColumn {
                        items(mockCredentials, key = { it.id }) { credential ->
                            OutlinedButton(
                                onClick = { onSelectCredential(credential) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .testTag(CREDENTIAL_ITEM_TAG)
                            ) {
                                Text(credential.displayName)
                            }
                        }
                    }
                }
            }
        }
    }

    fun NavController.navigateToVerifierAttributesSelection(
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.SelectAttributes,
        options
    )

    internal fun NavGraphBuilder.configureVerifierAttributesSelection(
        onSelectAttributeGroup: (AttributeGroup) -> Unit = {}
    ) {
        dialog<CredentialSharingDestination.SelectAttributes> {
            var selected by rememberSaveable {
                mutableStateOf(VerifierAttributeOption.PHOTO_AND_AGE_OVER_21)
            }

            Surface(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.select_attribute_group),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    VerifierAttributeOption.entries.forEach { option ->
                        OutlinedButton(
                            onClick = { selected = option },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag(ATTRIBUTE_GROUP_ITEM_TAG),
                            border = BorderStroke(
                                width = if (selected == option) 2.dp else 1.dp,
                                color = if (selected == option) Color.Blue else Color.Gray
                            )
                        ) {
                            RadioButton(
                                selected = selected == option,
                                onClick = { selected = option }
                            )
                            Text(option.displayName)
                        }
                    }
                    Button(
                        onClick = { onSelectAttributeGroup(selected.attributeGroup) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .testTag(VERIFY_CREDENTIAL_BUTTON_TAG)
                    ) {
                        Text(stringResource(R.string.verify_credential))
                    }
                }
            }
        }
    }

    internal fun NavGraphBuilder.configureTestAppHomeScreen(
        onStartHolderJourney: () -> Unit = {},
        onStartVerifierJourney: () -> Unit = {},
    ) {
        composable<CredentialSharingDestination.Undetermined> {
            TestAppScreen(
                modifier = Modifier.fillMaxSize(),
                onStartHolderJourney = onStartHolderJourney,
                onStartVerifierJourney = onStartVerifierJourney,
            )
        }
    }
}