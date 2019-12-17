package mobi.huibao.notebook.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ContactsResult extends Result {

    public DataBean data;

    public static class DataBean {
        public List<ContactsItem> list;
    }

    public static class ContactsItem implements Parcelable {

        Long id;

        String name;

        String phone;

        String icon;

        String company;

        String jobTitle;


        public ContactsItem() {
        }

        public ContactsItem(Long id, String name, String phone, String icon, String company, String jobTitle) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.icon = icon;
            this.company = company;
            this.jobTitle = jobTitle;
        }

        ContactsItem(Parcel in) {
            id = in.readLong();
            name = in.readString();
            phone = in.readString();
            icon = in.readString();
            company = in.readString();
            jobTitle = in.readString();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getIcon() {
            return icon;
        }

        public String getCompany() {
            return company;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(name);
            dest.writeString(phone);
            dest.writeString(icon);
            dest.writeString(company);
            dest.writeString(jobTitle);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ContactsItem> CREATOR = new Creator<ContactsItem>() {
            @Override
            public ContactsItem createFromParcel(Parcel in) {
                return new ContactsItem(in);
            }

            @Override
            public ContactsItem[] newArray(int size) {
                return new ContactsItem[size];
            }
        };
    }
}
