# DonateLife - Donor Management System

**DonateLife** is a simple Java-based donor management application that allows you to register, update, search, and track blood donors. The application features a graphical user interface (GUI) using Swing and supports donation eligibility checks, search filters, and data management.

---

## Features

- Add new donors with full details:
  - First Name, Last Name
  - Blood Type
  - Email & Phone
  - City
  - Last Donation Date
  - Active/Inactive status
- Update existing donor information
- Remove donors from the list
- Search/filter donors based on:
  - Blood Type
  - City
  - Donation eligibility
- Shows eligibility status based on last donation date (90-day waiting period)
- GUI built with **Swing**, including form input and table view

---

## Technologies

- Java 17+
- Swing (GUI)
- Collections & Streams
- UUID for unique identifiers
- LocalDateTime / Duration for date management

---

## Project Structure

DonateLife/
├── Donor.java          # Donor model class
├── DonorManager.java   # Donor list management class
├── DonorGUI.java       # Swing graphical user interface
└── README.md           # Instructions and documentation

## Notes

Phone numbers must be 10 digits.
Email is validated for basic format (must contain @ and .).
Last donation date cannot be in the future.
A donor is considered eligible if more than 90 days have passed since the last donation.


