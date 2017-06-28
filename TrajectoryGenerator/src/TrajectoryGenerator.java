import java.io.File;
import java.io.IOException;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;

public class TrajectoryGenerator {
	
	static String username = "lvuser";
	static String password = "";
	static String server = "10.32.56.2";
	static String rioPathBase = "/home/lvuser/";
	static int port = 22;
	static SSHClient ssh;
	
	public static void main(String[] args) throws IOException {
		generateCenterGearForwardPath();
		ssh = new SSHClient();
		//ssh.loadKnownHosts();
		ssh.addHostKeyVerifier("db:21:39:cc:ce:49:1b:b6:09:29:3e:72:1b:fb:fd:bd");
		ssh.connect(server);
		try{
			ssh.authPassword(username, password);
			SFTPClient sftp = ssh.newSFTPClient();
			generateCenterGearForwardPath();
			try{
				sftp.put(new FileSystemFile("C:\\Users\\Team 3256\\Documents\\GitHub\\FRC_Programming_2017\\TrajectoryGenerator\\CenterGearForwardPath.bin"), rioPathBase + "CenterGearForwardPath.bin");
			}
			finally{
				sftp.close();
			}
		}
		finally{
			ssh.close();
		}
	}
	
	public static void generateCenterGearForwardPath(){
		Waypoint[] points = new Waypoint[] {                      
			    new Waypoint(0, 0, Pathfinder.d2r(0)),
			    new Waypoint(77, 0, Pathfinder.d2r(0))
		};
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.01, 120, 108, 240);
		Trajectory trajectory = Pathfinder.generate(points, config);
		double t = 0;
		for (Segment segment : trajectory.segments) {
			t += segment.dt;
			System.out.println(t + ", " + segment.velocity);
		}
		File centerGearFowardPath = new File("CenterGearFowardPath.csv");
		Pathfinder.writeToCSV(centerGearFowardPath, trajectory);
		File centerGearForwardPathBinary = new File("CenterGearForwardPath.bin");
		Pathfinder.writeToFile(centerGearForwardPathBinary, trajectory);
	}
	
}