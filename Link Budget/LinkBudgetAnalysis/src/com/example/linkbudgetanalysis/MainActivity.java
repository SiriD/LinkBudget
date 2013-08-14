package com.example.linkbudgetanalysis;

import java.text.DecimalFormat;

import net.sf.doodleproject.numerics4j.special.Erf;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static double CF = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button bHelp = (Button) findViewById(R.id.bInfo);
		Button bComputeLB = (Button) findViewById(R.id.bLBA);
		bComputeLB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//data format
				DecimalFormat valFormat = new DecimalFormat("@@@"); 
				
				
				
				// Transmit antenna gain
				EditText etTxAntGain = (EditText) findViewById(R.id.etTxAntGain);
				
				// Median Path Loss
				TextView tvMedianPathLoss = (TextView) findViewById(R.id.tvMedPathLoss);
				final EditText etCarrierFreq = (EditText) findViewById(R.id.etCarrFreq);
				EditText etTxAntHt = (EditText) findViewById(R.id.etTxAntHt);
				final EditText etRxAntHt = (EditText) findViewById(R.id.etRxAntHt);
				EditText etCellRadius = (EditText) findViewById(R.id.etDistance);
				RadioButton rbSmallMedCity = (RadioButton) findViewById(R.id.rbOption1);
				RadioButton rbLargeCity = (RadioButton) findViewById(R.id.rbOption2);

				
				rbSmallMedCity.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						 CF = ((1.1 * Math.log10(Double
								.valueOf(etCarrierFreq.getText().toString())) - 0.7) * Double
								.valueOf(etRxAntHt.getText().toString()))
								- (1.56 * Math.log10(Double
										.valueOf(etCarrierFreq.getText()
												.toString()) - 0.8));

					}
				});

				rbLargeCity.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (Double.valueOf(etCarrierFreq.getText().toString()) <= 300) {
							CF = (8.29 * (Math.pow(Math
									.log10(1.54 * Double.valueOf(etRxAntHt
											.getText().toString())), 2))) - 1.1;

						}

						else {
							 CF = (3.2 * (Math.pow(Math
									.log10(11.75 * Double.valueOf(etRxAntHt
											.getText().toString())), 2))) - 4.97;

						}
					}
				});

				Double MPL = 69.55
						+ 26.16
						* Math.log10(Double.valueOf(etCarrierFreq.getText()
								.toString()))
						- CF
						- 13.82
						* Math.log10(Double.valueOf(etTxAntHt.getText()
								.toString()))
						+ (44.9 - 6.55 * Math.log10(Double.valueOf(etTxAntHt
								.getText().toString())))
						* Math.log10(Double.valueOf(etCellRadius.getText()
								.toString()));
				tvMedianPathLoss.setText(MPL.toString());

				// Margin Calculation
				TextView tvMarginCalc = (TextView) findViewById(R.id.tvMarginCalc);
				EditText etRelFactor = (EditText) findViewById(R.id.etRelFactor);
				EditText etDeviation = (EditText) findViewById(R.id.etDeviation);

				if (etDeviation.getText().toString() != null
						&& etRelFactor.getText().toString() != null
						&& etDeviation.getText().toString().length() != 0
						&& etRelFactor.getText().toString().length() != 0) {

					Double M = (Math.sqrt(2.0))
							* (Double.valueOf(etDeviation.getText().toString()))
							* (Erf.inverseErf((2.0 * Double.valueOf(etRelFactor
									.getText().toString())) - 1.0));

					tvMarginCalc.setText(M.toString());
				}

				// Receiver antenna gain
				EditText etRxAntGain = (EditText) findViewById(R.id.etRxAntGain);

				// Cabling Loss
				EditText etCablingLoss = (EditText) findViewById(R.id.etCableLoss);

				// Receive noise
				TextView tvRxNoise = (TextView) findViewById(R.id.tvRxNoiseCalc);
				EditText etConstant = (EditText) findViewById(R.id.etConstant);
				EditText etTemp = (EditText) findViewById(R.id.etTemp);
				EditText etBandwidth = (EditText) findViewById(R.id.etBandwidth);
				EditText etNF = (EditText) findViewById(R.id.etNoiseFig);

				if (etTemp.getText().toString() != null
						&& etNF.getText().toString() != null
						&& etBandwidth.getText().toString() != null
						&& etTemp.getText().toString().length() != 0
						&& etNF.getText().toString().length() != 0
						&& etBandwidth.getText().toString().length() != 0) {

					Double Con1, temp, F, noisePSD, total_noise, total_noise_dB, band;

					Con1 = 0.0000000000000000000000132;
					DecimalFormat f = new DecimalFormat("0.000E0");
					String Con = f.format(Con1);
					etConstant.setText(Con);

					temp = Double.valueOf(etTemp.getText().toString());
					F = Math.pow(10,
							0.1 * Double.valueOf(etNF.getText().toString()));

					noisePSD = Con1 * temp * F;

					band = Double.valueOf(etBandwidth.getText().toString());
					total_noise = noisePSD * band;
					total_noise_dB = 10 * Math.log10(total_noise);
					tvRxNoise.setText(total_noise_dB.toString());

				}
				// Required SNR
				TextView tvReqSNR = (TextView) findViewById(R.id.tvReqSNR);
				EditText etBER = (EditText) findViewById(R.id.etBER);
				Spinner spModulation = (Spinner) findViewById(R.id.spinner1);
				Double x;

				if (etBER.getText().toString() != null
						&& etBER.getText().toString().length() != 0) {

					DecimalFormat f = new DecimalFormat("00.E0");
					etBER.setText(f.format(Double.valueOf(etBER.getText()
							.toString())));

					if (spModulation.getSelectedItem().toString()
							.equals("BPSK")) {
						x = 2.0 * (Math.pow((Erf.inverseErf(1.0 - 2.0 * Double
								.valueOf(etBER.getText().toString()))), 2));
						tvReqSNR.setText(SNRdB(x).toString());
					}

					if (spModulation.getSelectedItem().toString()
							.equals("QPSK/4QAM")) {
						x = Double
								.valueOf(GetSNR(4, etBER.getText().toString()));
						tvReqSNR.setText(SNRdB(x).toString());
					}

					if (spModulation.getSelectedItem().toString()
							.equals("8PSK")) {
						x = Double
								.valueOf(GetSNR(8, etBER.getText().toString()));
						tvReqSNR.setText(SNRdB(x).toString());
					}

					if (spModulation.getSelectedItem().toString()
							.equals("16PSK")) {
						x = Double.valueOf(GetSNR(16, etBER.getText()
								.toString()));
						tvReqSNR.setText(SNRdB(x).toString());
					}

					if (spModulation.getSelectedItem().toString()
							.equals("BFSK")) {
						x = 0.5 * Double.valueOf(GetSNR(4, etBER.getText()
								.toString()));
						tvReqSNR.setText(SNRdB(x).toString());
					}

					if (spModulation.getSelectedItem().toString()
							.equals("16QAM")) {
						x = 2.0 * (Math.pow((Erf
								.inverseErf(1.0 - 0.6667 * Double.valueOf(etBER
										.getText().toString()))), 2));
						tvReqSNR.setText(SNRdB(x).toString());
					}

				}

				// Transmit power
				EditText etReqTxPower = (EditText) findViewById(R.id.etReqTxPower);
				Double reqTxPower = Double.valueOf(tvReqSNR.getText()
						.toString())
						+ Double.valueOf(tvRxNoise.getText().toString())
						+ Double.valueOf(etCablingLoss.getText().toString())
						- Double.valueOf(etRxAntGain.getText().toString())
						+ Double.valueOf(tvMarginCalc.getText().toString())
						+ Double.valueOf(tvMedianPathLoss.getText().toString())
						- Double.valueOf(etTxAntGain.getText().toString());
				etReqTxPower.setText(reqTxPower.toString());

			}
		});

		
		//Display Help Page
		
		bHelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, Help.class));
			
				
			}
		});
		
		
		
	}

	public double GetSNR(int x, String ber) {
		double snr = 0.0;
		snr = Math.pow(
				(Erf.inverseErf(1.0 - Double.valueOf(ber)))
						/ (Math.sin(Math.PI / x)), 2);
		return snr;
	}

	public Double SNRdB(double x) {
		double y = 0.0;
		y = 10.0 * Math.log10(x);
		return y;
	}
}
