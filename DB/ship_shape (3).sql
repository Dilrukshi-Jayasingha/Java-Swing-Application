-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 27, 2024 at 11:31 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ship_shape`
--

-- --------------------------------------------------------

--
-- Table structure for table `customerorders`
--

CREATE TABLE `customerorders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_category` varchar(255) NOT NULL,
  `item_description` text DEFAULT NULL,
  `item_quantity` int(11) NOT NULL,
  `special_note` text DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  `orderDate` date NOT NULL,
  `total_price` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customerorders`
--

INSERT INTO `customerorders` (`order_id`, `user_id`, `item_name`, `item_category`, `item_description`, `item_quantity`, `special_note`, `status`, `orderDate`, `total_price`) VALUES
(1, 1, 'Item Name one', 'Ship_Repair_Services', 'Desc', 3, 'No', 'reviewing', '2024-10-12', NULL),
(2, 1, 'Item name two', 'Ship_Repainting_Services', 'No', 4, 'NO', 'Processing', '2024-01-02', 500),
(3, 5, 'sample item', 'Ship_Repair_Services', 'sample des', 3, 'sample note', 'Completed', '2024-10-15', 100),
(5, 7, 'test item name', 'Ship_Repair_Services', 'des', 3, 'no one', 'Processing', '2024-10-15', 1000);

-- --------------------------------------------------------

--
-- Table structure for table `employeejobassignments`
--

CREATE TABLE `employeejobassignments` (
  `AssignmentID` int(11) NOT NULL,
  `JobID` int(11) NOT NULL,
  `EmployeeID` int(11) NOT NULL,
  `AssignmentDate` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `employeejobassignments`
--

INSERT INTO `employeejobassignments` (`AssignmentID`, `JobID`, `EmployeeID`, `AssignmentDate`) VALUES
(1, 1, 1, '2024-06-27 13:16:30'),
(2, 2, 2, '2024-06-27 14:27:47');

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `EmployeeID` int(11) NOT NULL,
  `EmployeeName` varchar(255) NOT NULL,
  `JobRole` varchar(255) NOT NULL,
  `Schedule` varchar(255) DEFAULT NULL,
  `Location` varchar(255) DEFAULT NULL,
  `ContactNumber` varchar(15) DEFAULT NULL,
  `Email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`EmployeeID`, `EmployeeName`, `JobRole`, `Schedule`, `Location`, `ContactNumber`, `Email`) VALUES
(1, 'charith', 'Mana', 'Day', 'Pili', '078', 'cha@gm'),
(2, 'test emp', 'maina', 'day', 'pili', '0785545545', 'c@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `inventoryitems`
--

CREATE TABLE `inventoryitems` (
  `ItemID` int(11) NOT NULL,
  `ItemName` varchar(255) NOT NULL,
  `Category` varchar(255) NOT NULL,
  `ReorderLevel` int(11) NOT NULL,
  `SupplierID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `inventoryitems`
--

INSERT INTO `inventoryitems` (`ItemID`, `ItemName`, `Category`, `ReorderLevel`, `SupplierID`) VALUES
(1, 'inven', 'cat', 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `inventorystock`
--

CREATE TABLE `inventorystock` (
  `StockID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL,
  `Location` varchar(255) NOT NULL,
  `Quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `inventorystock`
--

INSERT INTO `inventorystock` (`StockID`, `ItemID`, `Location`, `Quantity`) VALUES
(1, 1, 'pili', 10222),
(2, 2, 'colo', 12);

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `JobID` int(11) NOT NULL,
  `JobName` varchar(255) NOT NULL,
  `JobDescription` text DEFAULT NULL,
  `JobLocation` varchar(255) DEFAULT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobs`
--

INSERT INTO `jobs` (`JobID`, `JobName`, `JobDescription`, `JobLocation`, `StartDate`, `EndDate`) VALUES
(1, 'repair', 'repeir des', 'pili', '2022-10-15', '2023-10-15'),
(2, 'test job', 'dsa', 'sada', '2024-02-02', '2025-02-02');

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `SupplierID` int(11) NOT NULL,
  `SupplierName` varchar(255) NOT NULL,
  `Category` varchar(255) NOT NULL,
  `ContactPerson` varchar(255) DEFAULT NULL,
  `Email` varchar(255) NOT NULL,
  `PhoneNumber` varchar(20) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Country` varchar(100) DEFAULT NULL,
  `MarinePartsSupplied` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`SupplierID`, `SupplierName`, `Category`, `ContactPerson`, `Email`, `PhoneNumber`, `Address`, `Country`, `MarinePartsSupplied`) VALUES
(1, 'Sunmae', 'Ship_Repair_Services', 'cha', 'cha@gma', '078', 'add', 'sri', 'mann'),
(3, 'new test', 'Additional_Services', 'dassad', 'sss@gmail.com', '222222222', 'aad', 'sri', 'hhdhasd');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `address` text NOT NULL,
  `user_type` enum('Customer','Manager') NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `contact_number`, `address`, `user_type`, `password`) VALUES
(1, 'nnq', 'e', '0', 'a', 'Customer', '111'),
(2, 'nn', 'e', '0', 'a', 'Manager', '111'),
(3, 'nn', 'e', '', '', 'Manager', ''),
(4, 'na', 'e', 'cc', 'aa', 'Manager', '11'),
(5, 'Charitha Customer', 'cha@gmil', '078', 'addew', 'Customer', '123'),
(6, 'Charitha Manager', 'cha@gm', '078', 'address', 'Manager', '123'),
(7, 'Test Customer', 'cus@gmail.com', '078123455', 'Adrees', 'Customer', '123'),
(8, 'Test Manager', 'm@gmail.com', '12343333', 'ad', 'Manager', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customerorders`
--
ALTER TABLE `customerorders`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `employeejobassignments`
--
ALTER TABLE `employeejobassignments`
  ADD PRIMARY KEY (`AssignmentID`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`EmployeeID`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Indexes for table `inventoryitems`
--
ALTER TABLE `inventoryitems`
  ADD PRIMARY KEY (`ItemID`);

--
-- Indexes for table `inventorystock`
--
ALTER TABLE `inventorystock`
  ADD PRIMARY KEY (`StockID`);

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`JobID`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`SupplierID`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customerorders`
--
ALTER TABLE `customerorders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `employeejobassignments`
--
ALTER TABLE `employeejobassignments`
  MODIFY `AssignmentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
  MODIFY `EmployeeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `inventoryitems`
--
ALTER TABLE `inventoryitems`
  MODIFY `ItemID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `inventorystock`
--
ALTER TABLE `inventorystock`
  MODIFY `StockID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `jobs`
--
ALTER TABLE `jobs`
  MODIFY `JobID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `SupplierID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
