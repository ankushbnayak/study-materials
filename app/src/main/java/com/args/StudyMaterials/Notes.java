package com.args.StudyMaterials;


import com.google.firebase.database.Exclude;

//i am using this java class for
public class Notes
{
    String notes_url, notes_name,notes_branch;

    private String key;

    public String getNotes_url() {
        return notes_url;
    }

    public void setNotes_url(String notes_url) {
        this.notes_url = notes_url;
    }

    public String getNotes_name() {
        return notes_name;
    }

    public void setNotes_name(String notes_name) {
        this.notes_name = notes_name;
    }

    public String getNotes_branch() {
        return notes_branch;
    }

    public void setNotes_branch(String notes_branch) {
        this.notes_branch = notes_branch;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
