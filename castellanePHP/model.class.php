<?php
class Model
{
	private $dbh;

	public function __construct()
	{
		$this->dbh = null;
		try {
			$this->dbh = new PDO('mysql:host=localhost;dbname=castellane', 'richard', 'richard');
			$this->dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		} catch (Exception $e) {
			die($e->getMessage());
		}
	}

	public function check_connexion($email, $password)
	{
		if ($this->dbh!=null) {
			$request = "select * from users where email = :email and password = :password;" ;
			$data = array(":email"=>$email, ":password"=>$password);
			$select = $this->dbh->prepare($request);
			$select->execute($data);
			return $select->fetch();
		} else {
			return null;
			}
	}

	public function my_profile($email)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('SELECT * FROM users WHERE email LIKE :email');
			$stmt->bindValue(':email', $email);
			$stmt->execute();
			return $stmt->fetch(PDO::FETCH_ASSOC);
		}
		return null;
	}

	public function update_profile($iduser, $lastname, $firstname, $age, $phone, $email, $password)
	{
            if($this->dbh)
            {
                $request ="UPDATE users set lastname = :lastname, firstname = :firstname, age = :age,
                phone = :phone, email = :email, password = :password where iduser = :iduser";
                $data = array(":iduser"=>$iduser,
                				 ":lastname"=>$lastname, 
                                 ":firstname"=>$firstname,
                                 ":age"=>$age,
                                 ":phone"=>$phone,
                				 ":email"=>$email,
                				 ":password"=>$password); 
                $select = $this->dbh->prepare($request);
                $select->execute($data);
            }
		}

	public function get_shops($idshop)
	{
		if ($this->dbh) {
		$requete = "select * from shops where idshop = ".$idshop.";";
		error_log("requete => ".$requete);
		$select = $this->dbh->prepare($requete);
		$select->execute();
		return $select->fetchAll();
		} else {
				return null;
			}
	}

	public function my_annonce($title)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('SELECT * FROM news WHERE title LIKE :title');
			$stmt->bindValue(':title', $title);
			$stmt->execute();
			return $stmt->fetch(PDO::FETCH_ASSOC);
		}
		return null;
	}

	public function add_annonces($title, $content, $userid)
	{
		if ($this->dbh) {
		$requete = "INSERT INTO news values (null,'" .$title. "','" .$content. "'," .$userid.");";
		error_log("requete => ".$requete);
		$select = $this->dbh->prepare($requete);
		$select->execute();
		} 
	} 

	public function update_annonces($idnew, $title, $content, $userid)
	{
            if($this->dbh)
            {
                $request ="UPDATE news set title = :title, content = :content, userid = :userid WHERE idnew = :idnew";
                $data = array(":title"=>$title,
                				 ":content"=>$content, 
                                 ":userid"=>$userid,
                                 ":idnew"=>$idnew);
                $select = $this->dbh->prepare($request);
                $select->execute($data);
            }
	}

	public function deleteAnnonce($title)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('DELETE FROM news WHERE title LIKE :title');
			$stmt->bindValue(':title', $title);
			$stmt->execute();
		}
		return null;
	}

	public function my_vehicles($licenseplate)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('SELECT * FROM vehicles WHERE licenseplate LIKE :licenseplate');
			$stmt->bindValue(':licenseplate', $licenseplate);
			$stmt->execute();
			return $stmt->fetch(PDO::FETCH_ASSOC);
		}
		return null;
	}

	public function add_vehicles($licenceplate, $mileage, $brand, $status)
	{
		if ($this->dbh) {
		$requete = "INSERT INTO vehicles values (null,'" .$licenceplate. "'," .$mileage. ",
		'".$brand."','".$status."');";
		error_log("requete => ".$requete);
		$select = $this->dbh->prepare($requete);
		$select->execute();
		} 
	} 	

	public function update_vehicles($mileage, $status, $licenseplate)
	{
            if($this->dbh)
            {
                $request ="UPDATE vehicles set mileage = :mileage, `status` = :status WHERE licenseplate = :licenseplate";
                $data = array(":mileage"=>$mileage,
                				 ":status"=>$status, 
                                 ":licenseplate"=>$licenseplate);
                $select = $this->dbh->prepare($request);
                $select->execute($data);
            }
	}

	public function deleteVehicles($licenseplate)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('DELETE FROM vehicles WHERE licenseplate LIKE :licenseplate');
			$stmt->bindValue(':licenseplate', $licenseplate);
			$stmt->execute();
		}
		return null;
	}

	public function my_seance($idseance)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('SELECT * FROM seance WHERE idseance LIKE :idseance');
			$stmt->bindValue(':idseance', $idseance);
			$stmt->execute();
			return $stmt->fetch(PDO::FETCH_ASSOC);
		}
		return null;
	}

	public function add_seance($date, $time)
	{
		if ($this->dbh) {
		$requete = "INSERT INTO seance values (null,'" .$date. "','".$time."');";
		error_log("requete => ".$requete);
		$select = $this->dbh->prepare($requete);
		$select->execute();
		} 
	} 	 

	public function update_seance($idseance, $date, $time)
	{
            if($this->dbh)
            {
                $request ="UPDATE seance set date = :date, time = :time WHERE idseance = :idseance";
                $data = array(":date"=>$date,
                				 ":time"=>$time, 
                                 ":idseance"=>$idseance);
                $select = $this->dbh->prepare($request);
                $select->execute($data);
            }
	} 

	public function deleteSeance($idseance)
	{
		if ($this->dbh) {
			$stmt = $this->dbh->prepare('DELETE FROM seance WHERE idseance LIKE :idseance');
			$stmt->bindValue(':idseance', $idseance);
			$stmt->execute();
		}
		return null;
	}

	public function update_usersRight($email, $right)
	{
            if($this->dbh)
            {
                $request ="UPDATE users set `right` = :right where email = :email;";
                $data = array(":email"=>$email,
                				 ":right"=>$right);
                $select = $this->dbh->prepare($request);
                $select->execute($data);
            }
	}

	public function inscrireSeance($iduser, $idseance)
		{
		if ($this->dbh) {
		$requete = "INSERT INTO register_seance values ('" .$iduser. "','".$idseance."');";
		error_log("requete => ".$requete);
		$select = $this->dbh->prepare($requete);
		$select->execute();
		} 
	} 	

	public function get_MySeance($userid) 
	{
		$DB_USER='richard'; 
		$DB_PASS='richard'; 
		$DB_HOST='localhost';
		$DB_NAME='castellane';
		$mysqli = new mysqli($DB_HOST, $DB_USER, $DB_PASS, $DB_NAME);
		/* check connection */
		if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
		}		

		$mysqli->query("SET NAMES 'utf8'");
		$sql="SELECT * FROM seance_all where ID_User =" .$userid;
		$data = array(":userid"=>$userid);
		$result=$mysqli->query($sql);
		while($e=mysqli_fetch_assoc($result)){
		$output[]=$e; 
		}	

		print(json_encode($output)); 
		$mysqli->close();	

	}
} 