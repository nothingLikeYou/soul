/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.dromara.soul.common.constant;

/**
 * ZkPathConstants.
 *
 * @author xiaoyu(Myth)
 */
public final class ZkPathConstants implements Constants {

    /**
     * The constant SELECTOR_PARENT.
     */
    public static final String SELECTOR_PARENT = "/soul/selector";

    /**
     * The constant SELECTOR_JOIN_RULE.
     */
    public static final String SELECTOR_JOIN_RULE = "-";

    public static final String PLUGIN_PARENT = "/soul/plugin";

    private static final String RULE_PARENT = "/soul/rule";

    /**
     * The constant APP_AUTH_PARENT.
     */
    public static final String APP_AUTH_PARENT = "/soul/auth";

    /**
     * the constant LDO_TRAINING_USER_PARENT.
     */
    public static final String LDO_TRAINING_USER_PARENT = "/ldo/training/user";

    /**
     * the constant LDO_TRAINING_ROLE_PERM_RULE_PARENT.
     */
    public static final String LDO_TRAINING_ROLE_PERM_RULE_PARENT = "/ldo/training/role/perm/rule";

    /**
     * buildLdoTrainingUserPath.
     *
     * @param userCompanyId userCompanyId
     * @return zk path for ldo_training_user
     */
    public static String buildLdoTrainingUserPath(final String userCompanyId) {
        return String.join("/", LDO_TRAINING_USER_PARENT, userCompanyId);
    }

    /**
     * buildLdoTrainingUserParent
     *
     * @return
     */
    public static String buildLdoTrainingUserParent() {
        return String.join("/", LDO_TRAINING_USER_PARENT);
    }


    /**
     * buildLdoTrainingRolePermRuleParent
     *
     * @return zk parent for ldo_training_role_perm_rule
     */
    public static String buildLdoTrainingRolePermRuleParent() {
        return String.join("/", LDO_TRAINING_ROLE_PERM_RULE_PARENT);
    }

    /**
     * buildLdoTrainingRolePermRulePath
     *
     * @param url url
     * @return zk path for ldo_training_role_perm_rule
     */
    public static String buildLdoTrainingRolePermRulePath(final String url) {
        return String.join("/", LDO_TRAINING_ROLE_PERM_RULE_PARENT, url);
    }

    /**
     * acquire app_auth_path.
     *
     * @param appKey appKey
     * @return app_auth_path string
     */
    public static String buildAppAuthPath(final String appKey) {
        return String.join("/", APP_AUTH_PARENT, appKey);
    }

    /**
     * buildPluginParentPath.
     *
     * @return zk path for plugin
     */
    public static String buildPluginParentPath() {
        return String.join("/", PLUGIN_PARENT);
    }

    /**
     * buildPluginRealPath.
     *
     * @param pluginName pluginName
     * @return zk path for plugin
     */
    public static String buildPluginPath(final String pluginName) {
        return String.join("/", PLUGIN_PARENT, pluginName);
    }

    /**
     * buildSelectorParentPath.
     *
     * @param pluginName pluginName
     * @return zk path for selector
     */
    public static String buildSelectorParentPath(final String pluginName) {
        return String.join("/", SELECTOR_PARENT, pluginName);
    }

    /**
     * buildSelectorRealPath.
     *
     * @param pluginName pluginName
     * @param selectorId selectorId
     * @return zk full path for selector
     */
    public static String buildSelectorRealPath(final String pluginName, final String selectorId) {
        return String.join("/", SELECTOR_PARENT, pluginName, selectorId);
    }

    /**
     * buildRuleParentPath.
     *
     * @param pluginName pluginName
     * @return zk rule parent path.
     */
    public static String buildRuleParentPath(final String pluginName) {
        return String.join("/", RULE_PARENT, pluginName);
    }

    /**
     * buildRulePath.
     *
     * @param pluginName pluginName
     * @param selectorId selectorId
     * @param ruleId     ruleId
     * @return /soul/rule/pluginName/selectorId-ruleId
     */
    public static String buildRulePath(final String pluginName, final String selectorId, final String ruleId) {
        return String.join("/", buildRuleParentPath(pluginName), selectorId + SELECTOR_JOIN_RULE + ruleId);
    }

}
