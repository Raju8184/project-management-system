using System.Diagnostics;
class Launcher {
    static void Main(string[] args) {
        Process p = new Process();
        p.StartInfo.FileName = "java";
        p.StartInfo.Arguments = "-cp bin com.pms.Dashboard";
        p.StartInfo.UseShellExecute = false;
        p.StartInfo.CreateNoWindow = true; // This prevents the ugly black command prompt box!
        p.Start();
    }
}
