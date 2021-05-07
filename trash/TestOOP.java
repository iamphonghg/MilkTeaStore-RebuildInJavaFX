package trash;

import model.Staff;

public class TestOOP {
    public static void main(String[] args) {
        Staff staff = new Staff("NV0001", "Hoàng Gia Phong", 'M', "2000-10-27", "Manager", "0987654321", "working");
        Staff selectedStaff = staff;
        selectedStaff.setImageName("abc");
        System.out.println(staff == selectedStaff);
    }
}
