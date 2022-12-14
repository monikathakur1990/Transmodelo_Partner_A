package com.transmodelo.conductor.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripResponse {

    @SerializedName("account_status")
    @Expose
    private String accountStatus;
    @SerializedName("service_status")
    @Expose
    private String serviceStatus;
    @SerializedName("requests")
    @Expose
    private List<Request> requests = null;

    @SerializedName("provider_details")
    @Expose
    private Provider providerDetails;

    @SerializedName("servicelist")
    @Expose
    private  List<ServiceType> servicelist=null;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public Provider getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(Provider providerDetails) {
        this.providerDetails = providerDetails;

    }

    public List<ServiceType> getServicelist() {
        return servicelist;
    }

    public void setServicelist(List<ServiceType> servicelist) {
        this.servicelist = servicelist;
    }

    @Override
    public String toString() {
        return "TripResponse{" +
                "accountStatus='" + accountStatus + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", requests=" + requests.toString() +
                ", providerDetails=" + providerDetails.toString() +
                ", servicelist=" + servicelist.toString() +
                '}';
    }
}
