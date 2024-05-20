Calculator.jar: Manifest.txt bin/loan_windows/TitlePage.class
	jar -cfm Calculator.jar Manifest.txt -C bin .

bin/loan_windows/TitlePage.class:
	javac -d bin src/number_work/*.java src/swing_changes/*.java src/loan_calculator/*.java src/loan_windows/*.java

Manifest.txt:
	echo Main-Class: loan_windows.TitlePage > Manifest.txt