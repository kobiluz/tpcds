/*
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
 */

package io.trino.tpcds;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class ScalingInfo
{
    private static final double[] DEFINED_SCALES = {0, 1, 10, 100, 300, 1000, 3000, 10000, 30000, 100000};
    private int multiplier;
    private Map<Double, Integer> scalesToRowCountsMap;
    private boolean isNativeRowCount;

    public ScalingInfo(int multiplier, int[] rowCountsPerScale, int nativeRowCount)
    {
        checkArgument(multiplier >= 0, "multiplier is not greater than or equal to 0");
        this.multiplier = multiplier;
        this.isNativeRowCount = nativeRowCount != 0;
        checkArgument(rowCountsPerScale.length == DEFINED_SCALES.length);
        scalesToRowCountsMap = new HashMap<>(DEFINED_SCALES.length);
        for (int i = 0; i < rowCountsPerScale.length; i++) {
            checkArgument(rowCountsPerScale[i] >= 0, "row counts cannot be negative");
            scalesToRowCountsMap.put(DEFINED_SCALES[i], isNativeRowCount ? nativeRowCount : rowCountsPerScale[i]);
        }
    }

    public boolean isNativeRowCount()
    {
        return isNativeRowCount;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    public long getRowCountForScale(double scale)
    {
        checkArgument(scale <= 100000, "scale must be less than 100000");
        if (scalesToRowCountsMap.containsKey(scale)) {
            return scalesToRowCountsMap.get(scale);
        }
        throw new TpcdsException("unexpected scale " + scale);
    }
}
