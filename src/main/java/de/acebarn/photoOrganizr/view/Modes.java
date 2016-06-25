package de.acebarn.photoOrganizr.view;

/**
 * Created by alessio on 25.06.16.
 */
public enum Modes
{
    YEAR_MONTH_DAY("Jahr -> Monat -> Tag"),
    YEAR_MONTH("Jahr -> Monat");

    private String text;

    private Modes(String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return this.text;
    }
}
