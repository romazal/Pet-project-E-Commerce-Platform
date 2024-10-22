-- Create the databases if they don't exist

CREATE DATABASE IF NOT EXISTS customer;
CREATE DATABASE IF NOT EXISTS vendor;

GRANT ALL PRIVILEGES ON customer.* TO 'romazal'@'%';
GRANT ALL PRIVILEGES ON vendor.* TO 'romazal'@'%';
FLUSH PRIVILEGES;