package carsharing.company;

public class Company {
    private final String name;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Company)) {
            return false;
        } else {
            Company company = (Company) obj;
            return this.name.equals(company.name);
        }
    }
}
