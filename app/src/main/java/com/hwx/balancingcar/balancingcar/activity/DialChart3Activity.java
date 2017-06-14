/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库演示
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.hwx.balancingcar.balancingcar.activity;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hwx.balancingcar.balancingcar.R;
import com.hwx.balancingcar.balancingcar.view.DialChart03View;


public class DialChart3Activity extends Activity {

	DialChart03View chart = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial_chart3);
		
		chart = (DialChart03View)findViewById(R.id.circle_view); 

		final Button button = (Button)findViewById(R.id.button1); 
		
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int max = 100;
			    int min = 1;
				Random random = new Random();
				int p = random.nextInt(max)%(max-min+1) + min;					
				float pf = p / 100f;
				chart.setCurrentStatus(pf);
				chart.invalidate();
				
			}
			
		  }
		);
		
	}
}
