package com.happy3w.lotterycalculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

@SpringBootApplication
public class LotteryCalculatorApplication {

	public static void main(String[] args) throws IOException {
		LotteryCalculator lotteryCalculator = new LotteryCalculator();
//		SpringApplication.run(LotteryCalculatorApplication.class, args);
		InputStream inputStream = LotteryCalculatorApplication.class.getResourceAsStream("/lottery-history.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		LotteryInfo nextLottery = null;
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			LotteryInfo info = LotteryInfo.decode(line);

			LotteryInfo newLottery = lotteryCalculator.accept(info);
			System.out.println(MessageFormat.format("{0}:{1}--{2}--{3}",
					info.getCode(), info.getDesc(),
					nextLottery == null ? "": nextLottery.getDesc(),
					nextLottery == null ? "" : nextLottery.winPrice(info)));
			nextLottery = newLottery;
		}
	}

}
